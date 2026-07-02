package com.poc.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.backend.dto.PatientRegistrationRequest;
import com.poc.backend.dto.PatientRegistrationResponse;
import com.poc.backend.dto.PractitionerRegistrationRequest;
import com.poc.backend.dto.PractitionerRegistrationResponse;
import com.poc.backend.service.PatientRegistrationService;
import com.poc.backend.service.PractitionerRegistrationService;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final PatientRegistrationService patientRegistrationService;
    private final PractitionerRegistrationService practitionerRegistrationService;

    // Constructor Injection
    public RegistrationController(
            PatientRegistrationService patientRegistrationService,
            PractitionerRegistrationService practitionerRegistrationService
        ){
        this.patientRegistrationService = patientRegistrationService;
        this.practitionerRegistrationService = practitionerRegistrationService;
    }

    // Endpoint for registering Patient

    @PostMapping("/register/patient")
    public ResponseEntity<PatientRegistrationResponse> registerPatient(@RequestBody PatientRegistrationRequest request){
        return ResponseEntity.ok(patientRegistrationService.register(request));
    }

    // Endpoint for registering Practitioner
    
    @PostMapping("/register/doctor")
    public ResponseEntity<PractitionerRegistrationResponse> registerPractitioner(@RequestBody PractitionerRegistrationRequest request){
        return ResponseEntity.ok(practitionerRegistrationService.register(request));
    }

}
