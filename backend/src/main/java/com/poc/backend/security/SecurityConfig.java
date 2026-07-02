package com.poc.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.poc.backend.utility.JwtUtility;

@Configuration
public class SecurityConfig {

    private final JwtUtility jwtUtility;

    // Constructor injection for JwtUtility
    public SecurityConfig(JwtUtility jwtUtility){
        this.jwtUtility = jwtUtility;
    }

    // Define the JwtFilter bean
    @Bean
    public JwtFilter jwtFilter(){
        return new JwtFilter(jwtUtility);
    }
    
    // Define the PasswordEncoder bean
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Configure the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                                            .requestMatchers("/auth/**").permitAll()
                                            .requestMatchers("/Practitioner").permitAll()
                                            // fill all api endpoints acording to access permission
                                            .anyRequest()
                                            .authenticated())
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
                                       
    }

    // Define the AuthenticationManager bean
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

}
