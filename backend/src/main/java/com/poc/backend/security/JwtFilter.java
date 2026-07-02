package com.poc.backend.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.poc.backend.utility.JwtUtility;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter{

    private final JwtUtility jwtUtility;

    // 
    public JwtFilter(JwtUtility jwtUtility){
        this.jwtUtility = jwtUtility;
    }

    // Override the doFilterInternal method to handle JWT authentication
    // This method checks for the presence of a JWT in the Authorization header,
    // validates it, and sets the authentication in the SecurityContext if valid.
    // If the token is invalid or missing, it simply passes the request along the filter chain.
    // The filter does not throw an exception for invalid tokens; it allows the request to proceed without authentication.
    @Override
    protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain) throws ServletException, IOException{
                            String authHeader = request.getHeader("Authorization");

                            if (authHeader != null && authHeader.startsWith("Bearer ")){
                                String token = authHeader.substring(7);

                                if(jwtUtility.isValid(token)){
                                    String email = jwtUtility.extractEmail(token);
                                    String role = jwtUtility.extractRole(token);

                                    UsernamePasswordAuthenticationToken authentication 
                                                            = new UsernamePasswordAuthenticationToken(
                                                                                                email,
                                                                                                null,
                                                                                                List.of(new SimpleGrantedAuthority("ROLE_" + role)) // <-- if any access denied issue check here
                                    );
                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                                }
                            }

                            filterChain.doFilter(request, response);
                        }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        return path.startsWith("/auth/");
    }

}
