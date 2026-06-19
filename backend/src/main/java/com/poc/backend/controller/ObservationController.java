package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Observation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poc.backend.service.ObservationService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/Observation")
public class ObservationController {

    private final ObservationService service;
    private final FhirContext context = FhirContext.forR4();

    public ObservationController(ObservationService service){
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body){

        Observation observation = (Observation) context
                                            .newJsonParser()
                                            .parseResource(body);

        Observation created = service.create(observation);

        service.save(created);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(context
                        .newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(created));
    }

    // Update API

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable String id,
                                         @RequestBody String body){

        Observation observation = (Observation) context
                                            .newJsonParser()
                                            .parseResource(body);

        observation.setId(id);

        Observation updated = service.update(id, observation);

        service.save(updated);

        return ResponseEntity.ok(
                context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated));
    }
}
