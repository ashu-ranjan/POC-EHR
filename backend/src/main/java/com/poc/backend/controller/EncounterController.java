package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Encounter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poc.backend.service.EncounterService;

import ca.uhn.fhir.context.FhirContext;

@RestController
public class EncounterController {

    private final EncounterService encounterService;
    private final FhirContext context = FhirContext.forR4();

    // Constructor Injection
    public EncounterController(EncounterService encounterService){
        this.encounterService = encounterService;
    }

    // Create API

    @PostMapping("/Encounter")
    public String create(@RequestBody String body){

        Encounter encounter = (Encounter) context
                                .newJsonParser()
                                .parseResource(body);
                
        // Create in FHIR
        Encounter created = encounterService.createEncounter(encounter);

        // Save create in DB
        encounterService.saveEncounter(created);

        return context
                .newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(created);
    }

    // Update API

    @PutMapping("/Encounter/{id}")
    public String update(@PathVariable String id, @RequestBody String body){
        Encounter encounter = (Encounter) context
                                    .newJsonParser()
                                    .parseResource(body);
                               
        // Update in FHIR   
        Encounter updated = encounterService.updateEncounter(id, encounter);

        // Save update in DB
        encounterService.saveEncounter(encounter);

        return context.newJsonParser()
                    .setPrettyPrint(true)
                    .encodeResourceToString(updated);
    }

}
