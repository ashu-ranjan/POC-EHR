package com.poc.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PractitionerRegistrationRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String identifier;

    private String password;

}
