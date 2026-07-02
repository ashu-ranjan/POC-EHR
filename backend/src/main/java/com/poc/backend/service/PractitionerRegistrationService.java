package com.poc.backend.service;

import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poc.backend.dto.PractitionerRegistrationRequest;
import com.poc.backend.dto.PractitionerRegistrationResponse;
import com.poc.backend.entity.UserEntity;
import com.poc.backend.enums.Role;
import com.poc.backend.repository.PractitionerRepository;
import com.poc.backend.repository.UserRepository;

@Service
public class PractitionerRegistrationService {

    private final PractitionerService practitionerService;
    private final PractitionerRepository practitionerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection

    public PractitionerRegistrationService(
            PractitionerService practitionerService,
            PractitionerRepository practitionerRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ){
        this.practitionerService = practitionerService;
        this.practitionerRepository = practitionerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register Practitioner
    public PractitionerRegistrationResponse register(PractitionerRegistrationRequest request){

        // Check if email exists
        if(practitionerRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already registered");
        }

        // Check if Identifier exists
        if(practitionerRepository.existsByIdentifier(request.getIdentifier())){
            throw new RuntimeException("Identifier already registered");
        }

        // Check if User exists
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("User already exists");
        }

        // Practioner name, identifier registraion
        Practitioner practitioner = new Practitioner();

        practitioner.setActive(true);

        HumanName name = new HumanName();
        name.setFamily(request.getLastName());
        name.addGiven(request.getFirstName());

        practitioner.addName(name); // practioner name add

        practitioner.addTelecom().setSystem(ContactPointSystem.EMAIL).setValue(request.getEmail());
        Identifier identifier = new Identifier();

        identifier.setSystem("https://doctor-registry");
        identifier.setValue(request.getIdentifier());

        practitioner.addIdentifier(identifier); // Practitioner Identifier add

        // Create Practitioner to FHIR
        Practitioner createdPractitioner = practitionerService.create(practitioner);

        // Save create Practitioner to DB
        practitionerService.save(createdPractitioner);

        // Extracting practitioner id
        String practitionerId = createdPractitioner.getIdElement().getIdPart();

        // Create UserEntity and save to DB

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.DOCTOR);
        user.setReferenceId(practitionerId);

        userRepository.save(user);

        // Create response
        PractitionerRegistrationResponse response = new PractitionerRegistrationResponse();

        // Set response details
        response.setPractitionerId(practitionerId);
        response.setEmail(request.getEmail());
        response.setIdentifier(request.getIdentifier());
        response.setMessage("Practitioner registration successful");

        return response;
    }

}
