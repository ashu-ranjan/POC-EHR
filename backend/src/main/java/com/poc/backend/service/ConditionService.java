package com.poc.backend.service;

import org.hl7.fhir.r4.model.Condition;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.ConditionEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.ConditionMapper;
import com.poc.backend.repository.ConditionRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class ConditionService {

    private final ConditionRepository repository;
    private final IGenericClient fhirClient;

    public ConditionService(ConditionRepository repository,
                            IGenericClient fhirClient) {
        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE CONDITION

    // Create Condition to FHIR
    public Condition create(Condition condition) {

        // validation
        if (!condition.hasSubject()) {
            throw new BadRequestException("Patient reference is required.");
        }

        // identifier generation
        if (condition.getIdentifier().isEmpty()) {
            String identifierValue = IdGenerator.generateIdentifier("CON-", 5, 5);
            condition.addIdentifier().setValue(identifierValue);
        }

        // core creation
        try {
            return (Condition) fhirClient
                    .create()
                    .resource(condition)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create Condition.");
        }
    }

    // Save Condition to DB
    public ConditionEntity save(Condition condition) {

        try {
            String id = condition.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Condition/")
                    .toUriString() + id;

            return repository.save(
                    ConditionMapper.toEntity(condition, fullUrl, "match"));

        } catch (Exception e) {
            throw new DatabaseException("Failed to save Condition.");
        }
    }

    // UPDATE CONDITION
    
    public Condition update(String id, Condition condition) {

        // validation
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            Condition existing =
                    (Condition) fhirClient
                            .read()
                            .resource(Condition.class)
                            .withId(id)
                            .execute();

            // ALWAYS preserve identifier
            condition.setIdentifier(existing.getIdentifier());

            condition.setId(id);

            return (Condition) fhirClient
                    .update()
                    .resource(condition)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update Condition.");
        }
    }
}