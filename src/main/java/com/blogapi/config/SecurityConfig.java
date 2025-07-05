package com.blogapi.config;

import com.blogapi.jwt.JwtAuthFilter;
import com.blogapi.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    //authorize incoming requests from different URLs and grant authority as per roles
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()); // Optional for testing, required if using Postman etc.

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/blog/auth/**").permitAll()

                // Public GETs (all authenticated users)
                .requestMatchers(HttpMethod.GET, "/blog/posts/**", "/blog/comments/**")
                .hasAnyRole("USER", "AUTHOR", "ADMIN")

                // Post creation and updates – only Author & Admin
                .requestMatchers(HttpMethod.POST, "/blog/posts/**").hasAnyRole("AUTHOR", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/blog/posts/**").hasAnyRole("AUTHOR", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/blog/posts/**").hasAnyRole("AUTHOR", "ADMIN")

                // Admin access to other critical /blog endpoints
                .requestMatchers("/blog/**").hasRole("ADMIN")

                // Everything else must be authenticated
                .anyRequest().authenticated()
        );


        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(daoAuthenticationProvider);
    }

    //helper method
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();  // This is typically the email or username
        } else if (principal instanceof String username) {
            return username; // In case of anonymous or string-based principal
        }

        return null;
    }
}
