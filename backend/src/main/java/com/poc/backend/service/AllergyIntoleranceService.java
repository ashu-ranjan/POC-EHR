package com.poc.backend.service;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.AllergyIntoleranceEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.AllergyIntoleranceMapper;
import com.poc.backend.repository.AllergyIntoleranceRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class AllergyIntoleranceService {

    private final AllergyIntoleranceRepository repository;
    private final IGenericClient fhirClient;

    public AllergyIntoleranceService(AllergyIntoleranceRepository repository,
                                     IGenericClient fhirClient) {
        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE ALLERGY INTOLERANCE

    // Create Allergy Intolerance to FHIR
    public AllergyIntolerance create(AllergyIntolerance a) {

        // validation
        if(!a.hasPatient()){
            throw new BadRequestException("Patient reference is required.");
        }

        if(!a.hasCode()){
            throw new BadRequestException("Allergy code is required.");
        }

        // identifier generation
        if (a.getIdentifier().isEmpty()) {
            Identifier id = new Identifier();
            id.setValue(IdGenerator.generateIdentifier("ALL-", 5, 5));
            a.addIdentifier(id);
        }

        // core creation
        try {
            return (AllergyIntolerance) fhirClient
                .create()
                .resource(a)
                .execute()
                .getResource();
        } catch (Exception e) {
            throw new FHIRClientException("Failed to create AllergyIntolerance.");
        }
    }

    // Save Allergy Intolerance to DB
    public AllergyIntoleranceEntity save(AllergyIntolerance a) {

        try {
            String id = a.getIdElement().getIdPart();

            // generate full url for the particular entry
            String fullUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/AllergyIntolerance/")
                .toUriString() + id;

            AllergyIntoleranceEntity entity = AllergyIntoleranceMapper.toEntity(a, fullUrl, "match");

            // Preserve existing identifier 
            if (entity.getIdentifier() == null) {
                repository.findById(id)
                        .ifPresent(existing -> entity.setIdentifier(existing.getIdentifier()));
            }

            // save to db
            return repository.save(entity);

        } catch (Exception e) {
            throw new DatabaseException("Failed to save AllergyIntolerance.");
        }
    }

    // UPDATE ALLERGY INTOLERANCE
    
    public AllergyIntolerance update(String id, AllergyIntolerance a) {

        // validation
        if(id == null || id.isEmpty()){
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {
            a.setId(id);
            return (AllergyIntolerance) fhirClient
                .update()
                .resource(a)
                .execute()
                .getResource();
                
        } catch (Exception e) {
            throw new FHIRClientException("Failed to update AllergyIntolerance.");
        }
    }
}
