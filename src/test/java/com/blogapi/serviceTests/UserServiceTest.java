package com.blogapi.serviceTests;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blogapi.model.ROLE_TYPE;
import com.blogapi.model.User;
import com.blogapi.payload.AuthorSignUpResponse;
import com.blogapi.payload.SignUpRequest;
import com.blogapi.payload.SignUpResponse;
import com.blogapi.repository.UserRepository;
import com.blogapi.service.Implementation.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange - setup input and expected behavior
        SignUpRequest request = new SignUpRequest();
        request.setUsername("John black");
        request.setEmail("example@mail.com");
        request.setPassword("pass123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(request.getUsername());
        savedUser.setEmail(request.getEmail());
        savedUser.setPassword(request.getPassword());
        savedUser.setCreatedAt(LocalDateTime.now());
        savedUser.setRole(ROLE_TYPE.USER); // assuming default role

        // Mock repository save
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        // Act - call actual service
        SignUpResponse response = userService.registerUser(request);

        // Assert - verify expected output
        Assertions.assertNotNull(response);
        Assertions.assertEquals(Long.valueOf(1L), response.getId());
        Assertions.assertEquals("John black", response.getUsername());
        System.out.println("Register User Test Passed");
    }

    @Test
    void shouldRegisterAdminSuccessfully(){
        SignUpRequest request = new SignUpRequest();
        request.setUsername("Administrator");
        request.setEmail("admin@mail.com");
        request.setPassword("admin123");

        User savedAdmin = new User();
        savedAdmin.setId(2L);
        savedAdmin.setUsername(request.getUsername());
        savedAdmin.setEmail(request.getEmail());
        savedAdmin.setPassword(request.getPassword());
        savedAdmin.setCreatedAt(LocalDateTime.now());
        savedAdmin.setRole(ROLE_TYPE.ADMIN);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedAdmin);

        SignUpResponse response = userService.registerAdmin(request);

        Long id = 2L;
        Assertions.assertNotNull(response);
        Assertions.assertEquals(id, response.getId());
        Assertions.assertEquals("Administrator",response.getUsername());
        Assertions.assertEquals(ROLE_TYPE.ADMIN,response.getRole());
        System.out.println("Register Admin Passed");
    }

    @Test
    void shouldRegisterAuthorSuccessfully(){
        SignUpRequest request = new SignUpRequest();
        request.setUsername("Author");
        request.setEmail("author@mail.com");
        request.setPassword("author123");

        User savedAuthor = new User();
        savedAuthor.setId(2L);
        savedAuthor.setUsername(request.getUsername());
        savedAuthor.setEmail(request.getEmail());
        savedAuthor.setPassword(request.getPassword());
        savedAuthor.setCreatedAt(LocalDateTime.now());
        savedAuthor.setRole(ROLE_TYPE.AUTHOR);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedAuthor);

        AuthorSignUpResponse response = userService.registerAuthor(request);


        Long id = 2L;
        Assertions.assertNotNull(response); 
        Assertions.assertEquals(id, response.id());
        Assertions.assertEquals("Author", response.username());
        Assertions.assertEquals(ROLE_TYPE.AUTHOR, response.roleType());
        System.out.println("Register Author Passed");
        

    }

}
