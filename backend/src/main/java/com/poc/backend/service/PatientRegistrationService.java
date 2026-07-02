package com.poc.backend.service;

import java.sql.Date;

import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poc.backend.dto.PatientRegistrationRequest;
import com.poc.backend.dto.PatientRegistrationResponse;
import com.poc.backend.entity.UserEntity;
import com.poc.backend.enums.Role;
import com.poc.backend.repository.PatientRepository;
import com.poc.backend.repository.UserRepository;

@Service
public class PatientRegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final PatientService patientService;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public PatientRegistrationService(
            PasswordEncoder passwordEncoder,
            PatientService patientService,
            UserRepository userRepository,
            PatientRepository patientRepository
    ){
        this.passwordEncoder = passwordEncoder;
        this.patientRepository = patientRepository;
        this.patientService = patientService;
        this.userRepository = userRepository;
    }

    public PatientRegistrationResponse register(PatientRegistrationRequest request){

        // Check in user if email exists
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already registered kindly Login");
        }

        // Check in patient if email exists
        if(patientRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already registered kindly Login");
        }

        // Adding patient name and dob and email
        Patient patient = new Patient();

        HumanName name = new HumanName();

        name.setFamily(request.getLastName());
        name.addGiven(request.getFirstName());

        patient.addName(name); // Patient name add

        patient.setBirthDate(Date.valueOf(request.getDateOfBirth())); // Patient DOB add

        patient.addTelecom().setSystem(ContactPointSystem.EMAIL).setValue(request.getEmail()); // Patient Email add

        // Create Patient to FHIR
        Patient createdPatient = patientService.create(patient);

        // Save create patient to DB
        patientService.save(createdPatient);

        // Extracting Patient id from FHIR
        String patientId = createdPatient.getIdElement().getIdPart();

        // Creating User and save to db
        UserEntity user = new UserEntity();

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.PATIENT);
        user.setReferenceId(patientId);

        userRepository.save(user);

        // setting Patient response registration

        PatientRegistrationResponse response = new PatientRegistrationResponse();
        response.setPatientId(patientId);
        response.setEmail(request.getEmail());
        response.setMessage("Patient registration successful");
        
        return response;

    }

}
