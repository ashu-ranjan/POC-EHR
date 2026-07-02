package com.poc.backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.poc.backend.dto.LoginRequest;
import com.poc.backend.dto.LoginResponse;
import com.poc.backend.entity.UserEntity;
import com.poc.backend.repository.UserRepository;
import com.poc.backend.utility.JwtUtility;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtility jwtUtility;

    // Constructor injection for dependencies
    public LoginService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtUtility jwtUtility
    ){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtility = jwtUtility;
    }

    // Method to handle user login
    // It authenticates the user and generates a JWT token if successful
    // Throws RuntimeException if authentication fails or user is not found
    public LoginResponse login(LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(request.getEmail(), 
                                                                                    request.getPassword())
        );

        if(!authentication.isAuthenticated()){
            throw new RuntimeException("Invalid Credentials");
        }

        UserEntity user = userRepository
                                .findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("User Not Found!"));
                            
        String token = jwtUtility.generateToken(user.getEmail(), user.getRole().name(), user.getReferenceId());

        return new LoginResponse(
                token, 
                user.getRole().name(), 
                user.getReferenceId(), 
                user.getEmail());
    }

}
