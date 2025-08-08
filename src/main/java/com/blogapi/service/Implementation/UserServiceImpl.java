package com.blogapi.service.Implementation;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogapi.model.ROLE_TYPE;
import com.blogapi.model.User;
import com.blogapi.payload.AuthorSignUpResponse;
import com.blogapi.payload.SignUpRequest;
import com.blogapi.payload.SignUpResponse;
import com.blogapi.repository.UserRepository;
import com.blogapi.service.UserService;

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
            return new SignUpResponse(savedUser.getId(),savedUser.getUsername(),savedUser.getEmail(),savedUser.getRole());
    }

    @Override
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
        return new SignUpResponse(savedUser.getId(),savedUser.getUsername(),savedUser.getEmail(),savedUser.getRole());
    }

    @Override
    public AuthorSignUpResponse registerAuthor(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(ROLE_TYPE.AUTHOR);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return new AuthorSignUpResponse(savedUser.getId(),savedUser.getUsername(),savedUser.getEmail(),savedUser.getRole(),savedUser.isEnabled());

    }

//    @Override
//    public String approveAuthor(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        if (user.getRole() != ROLE_TYPE.AUTHOR) {
//            return "Only AUTHOR can be approved here";
//        }
//
//        user.setEnable(true);
//        userRepository.save(user);
//        return "Author approved successfully";
//    }
}
