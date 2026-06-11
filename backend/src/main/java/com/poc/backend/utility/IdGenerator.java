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

    // Patient Id generator

    public static String generatePatientId(String prefix, int minLen, int maxLen){
        return prefix + "-" + generate(minLen, maxLen);
    }

    // Practitioner Id generator

    public static String generateDoctorId(String prefix, int minLen, int maxLen){
        return prefix + "-" + generate(minLen, maxLen);
    }

    // Practitioner Identifier generator

    public static String generateDocIdentifier(String prefix, int minLen, int maxLen){
        return prefix + generate(minLen, maxLen);
    }

    // Organization Identifier generator

    public static String generateOrgIdentifier(String prefix, int minLen, int maxLen){
        return prefix + generate(minLen, maxLen);
    }

    // Location Identifier Generator

    public static String generateLocIdentifier(String prefix, int minLen, int maxLen){
        return prefix + generate(minLen, maxLen);
    }
    
}


