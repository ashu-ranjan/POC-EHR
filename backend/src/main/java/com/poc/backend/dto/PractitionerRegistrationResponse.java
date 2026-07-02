package com.poc.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PractitionerRegistrationResponse {

    private String PractitionerId;

    private String email;

    private String identifier;

    private String message;

}
