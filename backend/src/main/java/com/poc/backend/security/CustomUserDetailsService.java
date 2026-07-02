package com.poc.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.poc.backend.entity.UserEntity;
import com.poc.backend.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Constructor injection for UserRepository
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // Load user by username (email in this case) and return UserDetails
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        UserEntity user = userRepository.findByEmail(email)
                                            .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
        return new CustomUserDetails(user);
    }

}
