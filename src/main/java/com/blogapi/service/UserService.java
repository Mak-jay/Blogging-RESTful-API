package com.blogapi.service;

import com.blogapi.payload.AuthorSignUpResponse;
import com.blogapi.payload.SignUpRequest;
import com.blogapi.payload.SignUpResponse;
import com.blogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public SignUpResponse registerUser(SignUpRequest signUpRequest);
    public SignUpResponse registerAdmin(SignUpRequest signUpRequest);
    public AuthorSignUpResponse registerAuthor(SignUpRequest signUpRequest);
//    String approveAuthor(Long id);
}
