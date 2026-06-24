package com.poc.backend.service;

import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.DocumentReferenceEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.DocumentReferenceMapper;
import com.poc.backend.repository.DocumentReferenceRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class DocumentReferenceService {

    private final DocumentReferenceRepository repository;
    private final IGenericClient fhirClient;

    public DocumentReferenceService(
            DocumentReferenceRepository repository,
            IGenericClient fhirClient) {
        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE DOCUMENT REFERENCE

    // Create DocumentReference to FHIR
    public DocumentReference create(DocumentReference doc) {

        // validation (minimal)
        if (!doc.hasSubject()) {
            throw new BadRequestException("Patient reference is required.");
        }

        // identifier generation (masterIdentifier)
        if (!doc.hasMasterIdentifier()) {
            Identifier identifier = new Identifier();
            identifier.setValue(
                    IdGenerator.generateIdentifier("DOC-", 5, 5)
            );
            doc.setMasterIdentifier(identifier);
        }

        // core creation
        try {
            return (DocumentReference) fhirClient
                    .create()
                    .resource(doc)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create DocumentReference.");
        }
    }

    // Save DocumentReference to DB
    public DocumentReferenceEntity save(DocumentReference doc) {

        try {
            String id = doc.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/DocumentReference/")
                    .toUriString() + id;

            DocumentReferenceEntity entity = DocumentReferenceMapper.toEntity(doc, fullUrl, "match");
            
            if (entity.getIdentifier() == null) {
                repository.findById(id)
                        .ifPresent(existing -> entity.setIdentifier(existing.getIdentifier()));
            }
            return repository.save(entity);

        } catch (Exception e) {
            throw new DatabaseException("Failed to save DocumentReference.");
        }
    }

    // UPDATE DOCUMENT REFERENCE
    
    public DocumentReference update(String id, DocumentReference doc) {

        // validation
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            DocumentReference existing =
                    (DocumentReference) fhirClient
                            .read()
                            .resource(DocumentReference.class)
                            .withId(id)
                            .execute();

            // ALWAYS preserve masterIdentifier
            doc.setMasterIdentifier(existing.getMasterIdentifier());

            doc.setId(id);

            return (DocumentReference) fhirClient
                    .update()
                    .resource(doc)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update DocumentReference.");
        }
    }
}