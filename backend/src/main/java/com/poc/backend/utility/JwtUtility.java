package com.poc.backend.utility;

import org.springframework.stereotype.Component;

import com.poc.backend.security.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtility {

    private final JwtProperties jwtProperties;
    public JwtUtility(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
    }

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, String role, String refId){

        return Jwts.builder()
                    .subject(email)
                    .claim("role", role)
                    .claim("refId", refId)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                    .signWith(getSigningKey())
                    .compact();
    }

    public Claims extractClaims(String token){
        return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    public String extractEmail(String token){
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token){
        return extractClaims(token).get("role", String.class);
    }

    public String extractReferenceId(String token){
        return extractClaims(token).get("refId", String.class);
    }

    public boolean isValid(String token){
        try{
            
            Claims claims = extractClaims(token);
            return claims.getExpiration()
                .after(new Date());

        } catch(Exception e){
            return false;
        }
    }
    

}
