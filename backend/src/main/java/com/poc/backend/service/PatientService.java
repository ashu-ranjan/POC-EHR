package com.poc.backend.service;

import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.PatientEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.PatientMapper;
import com.poc.backend.repository.PatientRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class PatientService {

    private final PatientRepository repository;
    private final IGenericClient fhirClient;

    public PatientService(PatientRepository repository,
                          IGenericClient fhirClient) {
        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE PATIENT 

    // Create Patient to FHIR
    public Patient create(Patient patient){

        // validation
        if(!patient.hasIdentifier() && !patient.hasName()){
            throw new BadRequestException("Patient must have identifier or name.");
        }

        // identifier generation (optional fallback)
        if(patient.getIdentifier().isEmpty()){
            Identifier id = new Identifier();
            id.setValue(IdGenerator.generateIdentifier("PAT-", 5, 5));
            patient.addIdentifier(id);
        }

        // core creation
        try {
            return (Patient) fhirClient
                    .create()
                    .resource(patient)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create Patient.");
        }
    }

    // Save Patient to DB
    public PatientEntity save(Patient patient){

        try {
            String id = patient.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Patient/")
                    .toUriString() + id;
            
            PatientEntity entity = PatientMapper.patientEntity(patient, fullUrl, "match");
            
            if (entity.getIdentifier() == null) {
                repository.findById(id)
                        .ifPresent(existing -> entity.setIdentifier(existing.getIdentifier()));
            }
            return repository.save(entity);

        } catch (Exception e) {
            throw new DatabaseException("Failed to save Patient.");
        }
    }

    // UPDATE PATIENT
    
    public Patient update(String id, Patient patient){

        // validation
        if(id == null || id.isEmpty()){
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            Patient existing =
                    (Patient) fhirClient
                            .read()
                            .resource(Patient.class)
                            .withId(id)
                            .execute();

            // ALWAYS preserve identifier
            patient.setIdentifier(existing.getIdentifier());

            patient.setId(id);

            return (Patient) fhirClient
                    .update()
                    .resource(patient)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update Patient.");
        }
    }
}