package com.poc.backend.service;

import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.EncounterEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.EncounterMapper;
import com.poc.backend.repository.EncounterRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class EncounterService {

    private final EncounterRepository repository;
    private final IGenericClient fhirClient;

    public EncounterService(EncounterRepository repository,
                            IGenericClient fhirClient) {
        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE ENCOUNTER

    // Create Encounter to FHIR
    public Encounter create(Encounter encounter) {

        // validation
        if (!encounter.hasSubject()) {
            throw new BadRequestException("Patient reference is required.");
        }

        // identifier generation
        if (encounter.getIdentifier().isEmpty()) {
            Identifier id = new Identifier();
            id.setValue(IdGenerator.generateIdentifier("ENC-", 5, 5));
            encounter.addIdentifier(id);
        }

        // core creation
        try {
            return (Encounter) fhirClient
                    .create()
                    .resource(encounter)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create Encounter.");
        }
    }

    // Save Encounter to DB
    public EncounterEntity save(Encounter encounter) {

        try {
            String id = encounter.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Encounter/")
                    .toUriString() + id;

            EncounterEntity entity = EncounterMapper.toEntity(encounter, fullUrl, "match");

            
            if (entity.getIdentifier() == null) {
                repository.findById(id)
                        .ifPresent(existing -> entity.setIdentifier(existing.getIdentifier()));
            }

            return repository.save(entity);

        } catch (Exception e) {
            throw new DatabaseException("Failed to save Encounter.");
        }
    }

    // UPDATE ENCOUNTER
    
    public Encounter update(String id, Encounter encounter) {

        // validation
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            Encounter existing =
                    (Encounter) fhirClient
                            .read()
                            .resource(Encounter.class)
                            .withId(id)
                            .execute();

            // ALWAYS preserve identifier
            encounter.setIdentifier(existing.getIdentifier());

            encounter.setId(id);

            return (Encounter) fhirClient
                    .update()
                    .resource(encounter)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update Encounter.");
        }
    }
}
