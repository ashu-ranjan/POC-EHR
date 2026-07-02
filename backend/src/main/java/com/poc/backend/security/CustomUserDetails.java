package com.poc.backend.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.poc.backend.entity.UserEntity;

public class CustomUserDetails implements UserDetails{

    private final UserEntity user;

    // Constructor to initialize CustomUserDetails with a UserEntity
    public CustomUserDetails(UserEntity user){
        this.user = user;
    }

    // Getter for the reference ID of the user
    public String getReferenceId(){
        return user.getReferenceId();
    }

    // Getter for the role of the user
    public String getRole(){
        return user.getRole().name();
    }

    // Getter for the UserEntity object
    public UserEntity getUser(){
        return user;
    }

    // Implementing methods from UserDetails interface
   
    // Returns the authorities granted to the user
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    // Returns the password used to authenticate the user
    @Override
    public String getPassword(){
        return user.getPassword();
    }

    // Returns the authorities granted to the user
    @Override
    public String getUsername(){
        return user.getEmail();
    }

    // Indicates whether the user's account has expired
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    // Indicates whether the user is locked or unlocked
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    // Indicates whether the user's credentials (password) has expired
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    // Indicates whether the user is enabled or disabled
    @Override
    public boolean isEnabled(){
        return true;
    }
}
