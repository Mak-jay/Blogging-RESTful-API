package com.blogapi.controller;

import com.blogapi.exception.ResourceNotFoundException;
import com.blogapi.model.ROLE_TYPE;
import com.blogapi.model.User;
import com.blogapi.payload.AuthorSignUpResponse;
import com.blogapi.payload.LoginRequest;
import com.blogapi.jwt.JwtUtility;
import com.blogapi.payload.SignUpRequest;
import com.blogapi.payload.SignUpResponse;
import com.blogapi.repository.UserRepository;
import com.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login(@RequestBody LoginRequest user){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
            );
            return jwtUtility.generateToken(user.getUsername());
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/signUp/user")
    public ResponseEntity<SignUpResponse> registerUser(@RequestBody SignUpRequest signUpRequest){
        SignUpResponse signUpResponse = userService.registerUser(signUpRequest);
        return ResponseEntity.ok(signUpResponse);
    }

    @PostMapping("/signUp/author")
    public ResponseEntity<AuthorSignUpResponse> registerAuthor(@RequestBody SignUpRequest signUpRequest){
        AuthorSignUpResponse signUpResponse = userService.registerAuthor(signUpRequest);
        return ResponseEntity.ok(signUpResponse);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/approve-author/{id}")
    public ResponseEntity<String> approveAuthor(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != ROLE_TYPE.AUTHOR) {
            return ResponseEntity.badRequest().body("Only authors can be approved here");
        }

        user.setEnable(true);
        userRepository.save(user);

        return ResponseEntity.ok("Author approved successfully");
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/singUp/admin")
    public ResponseEntity<SignUpResponse> registerAdmin(@RequestBody SignUpRequest signUpAdmin){
        SignUpResponse signUpResponse = userService.registerAdmin(signUpAdmin);
        return ResponseEntity.ok(signUpResponse);

    }
}
