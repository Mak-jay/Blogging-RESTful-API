package com.blogapi.service.Implementation;

import com.blogapi.model.ROLE_TYPE;
import com.blogapi.model.User;
import com.blogapi.payload.SignUpRequest;
import com.blogapi.payload.SignUpResponse;
import com.blogapi.repository.UserRepository;
import com.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SignUpResponse registerUser(SignUpRequest signUpRequest) {
        if (userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            throw new RuntimeException("User is already exist");
        }
            User users = new User();
            users.setUsername(signUpRequest.getUsername());
            users.setEmail(signUpRequest.getEmail());
            users.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            users.setRole(ROLE_TYPE.USER);
            users.setCreatedAt(LocalDateTime.now());
            User savedUser = userRepository.save(users);
            return new SignUpResponse(savedUser.getId(),savedUser.getUsername(),savedUser.getRole());
    }

    public SignUpResponse registerAdmin(SignUpRequest signUpRequest) {
        if (userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            throw new RuntimeException("User is already exist");
        }
        User users = new User();
        users.setUsername(signUpRequest.getUsername());
        users.setEmail(signUpRequest.getEmail());
        users.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        users.setRole(ROLE_TYPE.ADMIN);
        users.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(users);
        return new SignUpResponse(savedUser.getId(),savedUser.getUsername(),savedUser.getRole());
    }
}
