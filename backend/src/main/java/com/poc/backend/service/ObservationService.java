package com.poc.backend.service;

import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.ObservationEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.ObservationMapper;
import com.poc.backend.repository.ObservationRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class ObservationService {

    private final ObservationRepository repository;
    private final IGenericClient fhirClient;

    public ObservationService(ObservationRepository repository,
                              IGenericClient fhirClient){
        this.fhirClient = fhirClient;
        this.repository = repository;
    }

    // CREATE OBSERVATION

    // Create Observation to FHIR
    public Observation create(Observation observation){

        // validation
        if(!observation.hasSubject()){
            throw new BadRequestException("Patient reference is required.");
        }

        // identifier generation
        if(observation.getIdentifier().isEmpty()){
            Identifier id = new Identifier();
            id.setValue(IdGenerator.generateIdentifier("OBS-", 5, 5));
            observation.addIdentifier(id);
        }

        // core creation
        try {
            return (Observation) fhirClient
                    .create()
                    .resource(observation)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create Observation.");
        }
    }

    // Save Observation to DB
    public ObservationEntity save(Observation observation){

        try {
            String id = observation.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Observation/")
                    .toUriString() + id;


            ObservationEntity entity = ObservationMapper.toEntity(observation, fullUrl, "match");
            
            if (entity.getIdentifier() == null) {
                repository.findById(id)
                        .ifPresent(existing -> entity.setIdentifier(existing.getIdentifier()));
            }
            return repository.save(entity);

        } catch (Exception e) {
            throw new DatabaseException("Failed to save Observation.");
        }
    }

    // UPDATE OBSERVATION
    
    public Observation update(String id, Observation observation){

        // validation
        if(id == null || id.isEmpty()){
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            Observation existing =
                    (Observation) fhirClient
                            .read()
                            .resource(Observation.class)
                            .withId(id)
                            .execute();

            // ALWAYS preserve identifier
            observation.setIdentifier(existing.getIdentifier());

            observation.setId(id);

            return (Observation) fhirClient
                    .update()
                    .resource(observation)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update Observation.");
        }
    }
}