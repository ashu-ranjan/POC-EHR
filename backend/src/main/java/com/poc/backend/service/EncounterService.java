package com.poc.backend.service;

import org.hl7.fhir.r4.model.Encounter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.EncounterEntity;
import com.poc.backend.mapper.EncounterMapper;
import com.poc.backend.repository.EncounterRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class EncounterService {

    private final EncounterRepository encounterRepository;
    private final IGenericClient fhirClient;

    // Constructor Injection
    public EncounterService(EncounterRepository encounterRepository, IGenericClient fhirClient){
        this.encounterRepository = encounterRepository;
        this.fhirClient = fhirClient;
    }

    // CREATE ENCOUNTER
    public EncounterEntity saveEncounter(Encounter encounter){

        String id = encounter.getIdElement().getIdPart();

        if(encounter.getIdentifier().isEmpty()){
            String indentifierValue = IdGenerator.generateIdentifier("ENC-", 5, 5);
            encounter.addIdentifier()
                        .setValue(indentifierValue);
        }
        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/Encounter/")
                .toUriString();
        String fullUrl = baseUrl + id;

        EncounterEntity entity = EncounterMapper.toEntity(encounter, fullUrl, "match");

        return encounterRepository.save(entity);

    }

    public Encounter createEncounter(Encounter encounter){

        return (Encounter) fhirClient
                        .create()
                        .resource(encounter)
                        .execute()
                        .getResource();

    }

    // UPDATE ENCOUNTER

    public Encounter updateEncounter(String id, Encounter encounter){
        encounter.setId(id);
        return (Encounter) fhirClient
                        .update()
                        .resource(encounter)
                        .execute()
                        .getResource();
    }
}
