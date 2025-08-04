package com.blogapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.blogapi.jwt.JwtAuthFilter;
import com.blogapi.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/blog/auth/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/info").permitAll()

                // Public GETs (all authenticated users)
                .requestMatchers(HttpMethod.GET, "/blog/posts/**", "/blog/comments/**")
                .hasAnyRole("USER", "AUTHOR", "ADMIN")

                // Post creation and updates – only Author & Admin
                .requestMatchers(HttpMethod.POST, "/blog/posts/**").hasAnyRole("AUTHOR", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/blog/posts/**").hasAnyRole("AUTHOR", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/blog/posts/**").hasAnyRole("AUTHOR", "ADMIN")

                // Comment operations - all authenticated users can create, but only authors/admins can modify
                .requestMatchers(HttpMethod.POST, "/blog/comments/**").hasAnyRole("USER", "AUTHOR", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/blog/comments/**").hasAnyRole("USER", "AUTHOR", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/blog/comments/**").hasAnyRole("USER", "AUTHOR", "ADMIN")

                // Admin access to other critical /blog endpoints
                .requestMatchers("/blog/**").hasRole("ADMIN")

                // Everything else must be authenticated
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

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
