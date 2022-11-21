package htw.webtech.bringify.service;

import htw.webtech.bringify.persistence.ConfirmationTokenEntity;
import htw.webtech.bringify.persistence.ConfirmationTokenRepository;
import htw.webtech.bringify.persistence.UserEntity;
import htw.webtech.bringify.security.email.EmailSender;
import htw.webtech.bringify.security.email.EmailTemp;
import htw.webtech.bringify.web.api.User;
import htw.webtech.bringify.persistence.UserRepository;
import htw.webtech.bringify.web.api.UserManipulationRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSender emailSender;

    private final EmailTemp emailTemp = new EmailTemp();
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenService confirmationTokenService, ConfirmationTokenRepository confirmationTokenRepository, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSender = emailSender;
    }

    public List<User> findAll(){
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(this:: transformEntity).collect(Collectors.toList());
    }

    public User create(UserManipulationRequest request){

        if(userRepository.findByMail(request.getMail()).isPresent()){
            throw new IllegalStateException("email allready taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        var userEntity = new UserEntity(request.getMail(),request.getUsername(), encodedPassword);
        userEntity = userRepository.save(userEntity);
        String token = UUID.randomUUID().toString();
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),userEntity);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        emailSender.send(request.getMail(),emailTemp.buildEmail(request.getUsername(),"https://bringify.herokuapp.com/api/v1/confirm?token="+token)); //muss für heroku geändert werden
        return transformEntity(userEntity);
    }
    public User findById(Long id) {
        var userEntity = userRepository.findById(id);
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

        return transformEntity(userEntity);
    }

    public boolean deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
    private User transformEntity(UserEntity userEntity){
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
            throw new IllegalStateException("token expired");
        }
        enableAppUser(confirmationToken.getUser().getMail());
        deleteConfirmationToken(token);
        return "confirmed";
    }

    public int enableAppUser(String mail) {
        return userRepository.enableAppUser(mail);
    }
    public void deleteConfirmationToken(String token){
        confirmationTokenRepository.deleteConfirmationTokenByToken(token);
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        return userRepository.findByMail(mail).orElseThrow(()-> new UsernameNotFoundException(""));
    }
}
