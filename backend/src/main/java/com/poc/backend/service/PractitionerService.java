package com.poc.backend.service;

import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.PractitionerEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.PractitionerMapper;
import com.poc.backend.repository.PractitionerRepository;
import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class PractitionerService {

    private final PractitionerRepository repository;
    private final IGenericClient fhirClient;

    public PractitionerService(PractitionerRepository repository,
                               IGenericClient fhirClient) {
        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE PRACTITIONER

    // Create Practitioner to FHIR
    public Practitioner create(Practitioner practitioner){

        // validation
        if(!practitioner.hasName()){
            throw new BadRequestException("Practitioner name is required.");
        }
 
        // identifier validation
        if(practitioner.getIdentifier().isEmpty()){
            throw new BadRequestException(
                    "Practitioner identifier is required."
            );
        }

        String identifier =
                practitioner.getIdentifierFirstRep().getValue();

        if(repository.existsByIdentifier(identifier)){
            throw new BadRequestException(
                    "Practitioner identifier already exists."
            );
        }

        // Email validation
        
        if(practitioner.getTelecom().isEmpty()){
                throw new BadRequestException(
                        "Practitioner email is required.");
            }

            String email =
                    practitioner.getTelecomFirstRep()
                                .getValue();

            if(repository.existsByEmail(email)){
                throw new BadRequestException(
                        "Practitioner email already exists.");
            }

        // core creation
        try {
            return (Practitioner) fhirClient
                    .create()
                    .resource(practitioner)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create Practitioner.");
        }
    }

    // Save Practitioner to DB
    public PractitionerEntity save(Practitioner practitioner){

        try {
            String id = practitioner.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Practitioner/")
                    .toUriString() + id;

            PractitionerEntity entity = PractitionerMapper.toEntity(practitioner, fullUrl, "match");
            
            if (entity.getIdentifier() == null) {
                repository.findById(id)
                        .ifPresent(existing -> entity.setIdentifier(existing.getIdentifier()));
            }
            return repository.save(entity);

        } catch (Exception e) {
            throw new DatabaseException("Failed to save Practitioner.");
        }
    }

    // UPDATE PRACTITIONER
    
    public Practitioner update(String id, Practitioner practitioner){

        // validation
        if(id == null || id.isEmpty()){
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            Practitioner existing =
                    (Practitioner) fhirClient
                            .read()
                            .resource(Practitioner.class)
                            .withId(id)
                            .execute();

            // ALWAYS preserve identifier
            practitioner.setIdentifier(existing.getIdentifier());

            practitioner.setId(id);

            return (Practitioner) fhirClient
                    .update()
                    .resource(practitioner)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update Practitioner.");
        }
    }
}