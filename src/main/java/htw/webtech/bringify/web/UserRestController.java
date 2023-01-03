package htw.webtech.bringify.web;

import htw.webtech.bringify.persistence.*;
import htw.webtech.bringify.security.jwt.JwtUtil;
import htw.webtech.bringify.security.jwt.dto.JwtResponse;
import htw.webtech.bringify.security.jwt.dto.SignInRequest;
import htw.webtech.bringify.service.UserService;
import htw.webtech.bringify.web.api.*;
import htw.webtech.bringify.web.api.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class UserRestController {


    private UserRepository userRepository;
    private RolesRepository rolesRepository;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private final UserService userService;

    public UserRestController(UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              RolesRepository rolesRepository,
                             UserService userService,ConfirmationTokenRepository confirmationTokenRepository,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping(path = "/api/v1/users")
    public ResponseEntity<List<User>> fetchUser(){
        return ResponseEntity.ok(userService.findAll());
    }



    @PutMapping(path = "/api/v1/confirm/{token}")
    public String confirm(@PathVariable String token){
        return userService.confirmToken(token);
    }

    @GetMapping(path = "/api/v1/users/{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable Long id) {
        var user = userService.findById(id);
        return user != null? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/api/v1/users_mail/{mail}")
    public ResponseEntity<User> fetchUserByEmail(@PathVariable String mail){
        var user = userService.findByMail(mail);
        return  user !=null? ResponseEntity.ok(user) :ResponseEntity.notFound().build();
    }


    @PutMapping(path = "/api/v1/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserManipulationRequest request) {
        var user = userService.update(id, request);
        return user != null? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/api/v1/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean successful = userService.deleteById(id);
        return successful? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/api/v1/current_user")
    public UserId getCurrentUser(@RequestBody Token token){
        String username = jwtUtil.getUserNameFromJwtToken(token.getToken());
        UserEntity userEntity = userRepository.findByUsername(username).get();
        User user = userService.transformEntity(userEntity);
        return new UserId(user.getId());
    }
    @PostMapping("/api/v1/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest) {
        if(!userRepository.existsByUsername(signInRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserEntity user =  userRepository.findByUsername(signInRequest.getUsername()).get();
        if(user.isEnabled() == false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not verified");
        }
        JwtResponse res = userService.login(signInRequest);
        System.out.println(res.getToken());
        return ResponseEntity.ok(res);
    }
    @PostMapping("/api/v1/signup")
    public ResponseEntity<?> signup(@RequestBody UserManipulationRequest userManipulationRequest) throws URISyntaxException {

        if(userRepository.existsByUsername(userManipulationRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already taken");
        }
        if (userRepository.existsByMail(userManipulationRequest.getMail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already taken");
        }
        var user = userService.create(userManipulationRequest);
        String hashedPassword = passwordEncoder.encode(userManipulationRequest.getPassword());
        URI uri = new URI("/api/v1/users"+ user.getId());
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/api/v1/request_email")
    public ResponseEntity<Void> requestEmail(@RequestBody ResetEmailRequest resetEmailRequest){
        userService.sendResetEmail(resetEmailRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/reset_password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        HttpStatus status =userService.resetPassword(resetPasswordRequest);
        if(status == HttpStatus.OK){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

}
