package com.poc.backend.utility;

import java.security.SecureRandom;

public class IdGenerator {

    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final SecureRandom random = new SecureRandom();

    public static String generate(int minLen, int maxLen){
        int len = random.nextInt((maxLen - minLen) + 1) + minLen;
        StringBuilder id = new StringBuilder(len);
        for (int i = 0; i<len; i++){
            id.append(ALPHANUM.charAt(random.nextInt(ALPHANUM.length())));
        }
        return id.toString();
    }

    


    
    // Identifier Generator

    public static String generateIdentifier(String prefix, int minLen, int maxLen){
        return prefix + generate(minLen, maxLen);
    }
    
}


