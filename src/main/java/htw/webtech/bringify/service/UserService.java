package htw.webtech.bringify.service;

import htw.webtech.bringify.persistence.*;
import htw.webtech.bringify.security.email.EmailTempConfirm;
import htw.webtech.bringify.security.email.EmailSender;
import htw.webtech.bringify.security.email.EmailTempReset;
import htw.webtech.bringify.security.jwt.JwtUtil;
import htw.webtech.bringify.security.jwt.UserDetailsImpl;
import htw.webtech.bringify.security.jwt.dto.JwtResponse;
import htw.webtech.bringify.security.jwt.dto.SignInRequest;
import htw.webtech.bringify.web.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final ResetTokenService resetTokenService;
    private final ResetTokenRepository resetTokenRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSender emailSender;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private final EmailTempConfirm emailTempConfirm = new EmailTempConfirm();
    private final EmailTempReset emailTempReset = new EmailTempReset();
    public UserService( ItemRepository itemRepository,UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenService confirmationTokenService, ResetTokenService resetTokenService, ResetTokenRepository resetTokenRepository, ConfirmationTokenRepository confirmationTokenRepository, EmailSender emailSender, RoomRepository roomRepository, RoomService roomService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.resetTokenService = resetTokenService;
        this.resetTokenRepository = resetTokenRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSender = emailSender;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.itemRepository = itemRepository;
    }

    public List<User> findAll(){
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(this:: transformEntity).collect(Collectors.toList());
    }

    public User create(UserManipulationRequest request){

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        var userEntity = new UserEntity(request.getUsername(), request.getMail(),encodedPassword);
        userEntity = userRepository.save(userEntity);
        String token = UUID.randomUUID().toString();
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),userEntity);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        emailSender.sendConfirm(request.getMail(),emailTempConfirm.buildEmail(request.getUsername(),"http://localhost:8081/confirm?token="+token)); //muss für heroku geändert werden
        return transformEntity(userEntity);
    }
    public JwtResponse login(SignInRequest signInRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        JwtResponse res = new JwtResponse();
        res.setToken(jwt);
        res.setId(userDetails.getId());
        res.setUsername(userDetails.getUsername());
        res.setRoles(roles);
        return res;
    }
    public User findById(Long id) {
        var userEntity = userRepository.findById(id);
        return userEntity.map(this::transformEntity).orElse(null);
    }

    public User findByMail(String mail){
        var userEntity = userRepository.findByMail(mail);
        return userEntity.map(this::transformEntity).orElse(null);
    }

    public User update(Long id, UserManipulationRequest request) {
        var userEntityOptional = userRepository.findById(id);
        if (userEntityOptional.isEmpty()) {
            throw new IllegalStateException("");
        }

        var userEntity = userEntityOptional.get();
        userEntity.setMail(request.getMail());
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(request.getPassword());

        userRepository.save(userEntity);
        return transformEntity(userEntity);
    }

    public boolean deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
    public User transformEntity(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getMail(),
                userEntity.getPassword()
        );
    }

    public String confirmToken(String token){
        ConfirmationTokenEntity confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() -> new IllegalStateException("token not found"));
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            confirmationTokenRepository.deleteConfirmationTokenByToken(token);
            throw new IllegalStateException("token expired");
        }
        userRepository.enableUser(confirmationToken.getUser().getMail());
        confirmationTokenRepository.deleteConfirmationTokenByToken(token);
        return "confirmed";
    }

    public HttpStatus resetPassword(ResetPasswordRequest resetPasswordRequest){
        String token = resetPasswordRequest.getToken();
        if(!resetTokenRepository.existsByToken(token)){
            return HttpStatus.NOT_FOUND;
        }
        ResetTokenEntity resetTokenEntity = resetTokenService.getToken(token).get();

        LocalDateTime expiredAt = resetTokenEntity.getExpiresAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            resetTokenRepository.deleteResetTokenByToken(token);
            return HttpStatus.BAD_REQUEST;
        }

        userRepository.setPassword(resetTokenEntity.getUser().getMail(),bCryptPasswordEncoder.encode(resetPasswordRequest.getPassword()));

        resetTokenRepository.deleteResetTokenByToken(token);
        return HttpStatus.OK;
    }
    public void sendResetEmail(String email){
        UserEntity userEntity = userRepository.findByMail(email).get();
        String token = UUID.randomUUID().toString();
        ResetTokenEntity resetTokenEntity = new ResetTokenEntity(token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),userEntity);
        resetTokenService.saveResetToken(resetTokenEntity);
        emailSender.sendReset(email,emailTempReset.buildEmail(userEntity.getUsername(),"http://localhost:8081/new_password?token="+token));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username" + username));
        return UserDetailsImpl.build(user);
    }
    public List<Room> getAllRoomNamesFromUser(Long userId){
        List<Room> roomList = new ArrayList<>();
        List<RoomEntity> roomEntityList = roomRepository.findAll();
        for(RoomEntity i: roomEntityList){
            Set<UserEntity> userList = i.getUsers();
            for(UserEntity u:userList){
                if(u.getId() == userId){
                    roomList.add(roomService.roomEntityToRoom(i));
                }
            }
        }
        return roomList;

    }

    public Username setUsernameToItem(Long itemId, Long userId){
        UserEntity userEntity = userRepository.findById(userId).get();
        User user = transformEntity(userEntity);
        itemRepository.addUsername(user.getUsername(),itemId);
        return new Username(user.getUsername());
    }



}
