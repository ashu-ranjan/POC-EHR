package com.poc.backend.utility;

import java.util.regex.Pattern;

public class UserUtility {

    // Email pattern 
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Email validation
    public static boolean isValidEmail(String email){
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Password Validation
    public static boolean isValidPassword(String password){
        return password.length() >= 8
                && password.matches(".*[0-9].*")
                && password.matches(".*[!@#$%^&*].*");
    }

}
