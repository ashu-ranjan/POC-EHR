package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poc.backend.service.PractitionerService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/Practitioner")
public class PractitionerController {

    private final FhirContext context = FhirContext.forR4();
    private final PractitionerService service;

    public PractitionerController(PractitionerService service){
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body){

        Practitioner practitioner = (Practitioner) context
                                        .newJsonParser()
                                        .parseResource(body);

        Practitioner created = service.create(practitioner);

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

        Practitioner practitioner = (Practitioner) context
                                        .newJsonParser()
                                        .parseResource(body);

        practitioner.setId(id);

        Practitioner updated = service.update(id, practitioner);

        service.save(updated);

        return ResponseEntity.ok(
                context
                        .newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated));
    }
}