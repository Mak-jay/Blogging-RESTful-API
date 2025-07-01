package com.blogapi.controller;

import com.blogapi.payload.LoginRequest;
import com.blogapi.jwt.JwtUtility;
import com.blogapi.payload.SignUpRequest;
import com.blogapi.payload.SignUpResponse;
import com.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private UserService userService;

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

    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponse> registerUser(@RequestBody SignUpRequest signUpRequest){
        SignUpResponse signUpResponse = userService.registerUser(signUpRequest);
        return ResponseEntity.ok(signUpResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<SignUpResponse> registerAdmin(@RequestBody SignUpRequest signUpAdmin){
        SignUpResponse signUpResponse = userService.registerAdmin(signUpAdmin);
        return ResponseEntity.ok(signUpResponse);

    }
}
