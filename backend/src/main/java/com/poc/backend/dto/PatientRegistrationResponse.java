package com.poc.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRegistrationResponse {

    private String patientId;
    private String email;
    private String message;
}
