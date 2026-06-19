package com.poc.backend.service;

import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Procedure;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.ProcedureEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.ProcedureMapper;
import com.poc.backend.repository.ProcedureRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class ProcedureService {

    private final ProcedureRepository repository;
    private final IGenericClient fhirClient;

    public ProcedureService(
            ProcedureRepository repository,
            IGenericClient fhirClient) {

        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE PROCEDURE

    // Create Procedure to FHIR
    public Procedure create(Procedure procedure) {

        // validation
        if (!procedure.hasSubject()) {
            throw new BadRequestException("Patient reference is required.");
        }

        // identifier generation
        if (procedure.getIdentifier().isEmpty()) {

            Identifier identifier = new Identifier();
            identifier.setValue(
                    IdGenerator.generateIdentifier("PROC-", 5, 5)
            );

            procedure.addIdentifier(identifier);
        }

        // core creation
        try {
            return (Procedure) fhirClient
                    .create()
                    .resource(procedure)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create Procedure.");
        }
    }

    // Save Procedure to DB
    public ProcedureEntity save(Procedure procedure) {

        try {
            String id = procedure.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Procedure/")
                    .toUriString() + id;

            return repository.save(
                    ProcedureMapper.toEntity(procedure, fullUrl, "match"));

        } catch (Exception e) {
            throw new DatabaseException("Failed to save Procedure.");
        }
    }

    // UPDATE PROCEDURE
    
    public Procedure update(String id, Procedure procedure) {

        // validation
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            Procedure existing =
                    (Procedure) fhirClient
                            .read()
                            .resource(Procedure.class)
                            .withId(id)
                            .execute();

            // ALWAYS preserve identifier
            procedure.setIdentifier(existing.getIdentifier());

            procedure.setId(id);

            return (Procedure) fhirClient
                    .update()
                    .resource(procedure)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update Procedure.");
        }
    }
}
