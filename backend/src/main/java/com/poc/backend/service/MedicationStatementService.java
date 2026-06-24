package com.poc.backend.service;

import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.MedicationStatementEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.MedicationStatementMapper;
import com.poc.backend.repository.MedicationStatementRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class MedicationStatementService {

    private final MedicationStatementRepository repository;
    private final IGenericClient fhirClient;

    public MedicationStatementService(MedicationStatementRepository repository,
                                      IGenericClient fhirClient){
        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE MEDICATION STATEMENT

    // Create MedicationStatement to FHIR
    public MedicationStatement create(MedicationStatement ms){

        // validation
        if(!ms.hasSubject()){
            throw new BadRequestException("Patient reference is required.");
        }

        // identifier generation
        if(ms.getIdentifier().isEmpty()){
            Identifier id = new Identifier();
            id.setValue(IdGenerator.generateIdentifier("MED-", 5, 5));
            ms.addIdentifier(id);
        }

        // core creation
        try {
            return (MedicationStatement) fhirClient
                    .create()
                    .resource(ms)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create MedicationStatement.");
        }
    }

    // Save MedicationStatement to DB
    public MedicationStatementEntity save(MedicationStatement ms){

        try {
            String id = ms.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/MedicationStatement/")
                    .toUriString() + id;

            MedicationStatementEntity entity = MedicationStatementMapper.toEntity(ms, fullUrl, "match");
            
            if (entity.getIdentifier() == null) {
                repository.findById(id)
                        .ifPresent(existing -> entity.setIdentifier(existing.getIdentifier()));
            }
            return repository.save(entity);

        } catch (Exception e) {
            throw new DatabaseException("Failed to save MedicationStatement.");
        }
    }

    // UPDATE MEDICATION STATEMENT
    
    public MedicationStatement update(String id, MedicationStatement ms){

        // validation
        if(id == null || id.isEmpty()){
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            MedicationStatement existing =
                    (MedicationStatement) fhirClient
                            .read()
                            .resource(MedicationStatement.class)
                            .withId(id)
                            .execute();

            // ALWAYS preserve identifier
            ms.setIdentifier(existing.getIdentifier());

            ms.setId(id);

            return (MedicationStatement) fhirClient
                    .update()
                    .resource(ms)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update MedicationStatement.");
        }
    }
}