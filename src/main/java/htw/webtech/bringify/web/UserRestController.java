package htw.webtech.bringify.web;

import htw.webtech.bringify.persistence.*;
import htw.webtech.bringify.security.jwt.UserDetailsImpl;
import htw.webtech.bringify.security.jwt.dto.JwtResponse;
import htw.webtech.bringify.security.jwt.JwtUtil;
import htw.webtech.bringify.security.jwt.dto.SignInRequest;
import htw.webtech.bringify.security.jwt.dto.SignUpRequest;
import htw.webtech.bringify.service.UserService;
import htw.webtech.bringify.web.api.ResetEmailRequest;
import htw.webtech.bringify.web.api.ResetPasswordRequest;
import htw.webtech.bringify.web.api.User;
import htw.webtech.bringify.web.api.UserManipulationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class UserRestController {


    private UserRepository userRepository;
    private RolesRepository rolesRepository;
    private PasswordEncoder passwordEncoder;

    private final UserService userService;

    public UserRestController(UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              RolesRepository rolesRepository,
                             UserService userService) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;

        this.userService = userService;
    }


    @GetMapping(path = "/api/v1/users")
    public ResponseEntity<List<User>> fetchUser(){
        return ResponseEntity.ok(userService.findAll());
    }



    @GetMapping(path = "/api/v1/confirm")
    public String confirm(@RequestParam("token") String token){
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

    @GetMapping(path = "/api/v1/profile")
    public User getProfile(){
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        Authentication authentication = contextHolder.getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user;
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
