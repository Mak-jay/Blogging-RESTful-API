package com.blogapi.service.Implementation;

import com.blogapi.exception.ResourceNotFoundException;
import com.blogapi.model.ROLE_TYPE;
import com.blogapi.model.User;
import com.blogapi.payload.AuthorSignUpResponse;
import com.blogapi.payload.SignUpRequest;
import com.blogapi.payload.SignUpResponse;
import com.blogapi.repository.UserRepository;
import com.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
            return new SignUpResponse(savedUser.getId(),savedUser.getUsername(),savedUser.getEmail(),savedUser.getRole());
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
        return new SignUpResponse(savedUser.getId(),savedUser.getUsername(),savedUser.getEmail(),savedUser.getRole());
    }

    @Override
    public AuthorSignUpResponse registerAuthor(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(ROLE_TYPE.AUTHOR)
                .enable(false) // needs admin approval
                .build();

        userRepository.save(user);
        return new AuthorSignUpResponse(user.getId(),user.getUsername(),user.getEmail(),user.getRole(),user.isEnabled());

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
