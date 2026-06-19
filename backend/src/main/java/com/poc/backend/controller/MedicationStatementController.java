package com.poc.backend.controller;

import org.hl7.fhir.r4.model.MedicationStatement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poc.backend.service.MedicationStatementService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/MedicationStatement")
public class MedicationStatementController {

    private final MedicationStatementService service;
    private final FhirContext context = FhirContext.forR4();

    public MedicationStatementController(MedicationStatementService service){
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body){

        MedicationStatement ms = (MedicationStatement) context
                                    .newJsonParser()
                                    .parseResource(body);

        MedicationStatement created = service.create(ms);

        service.save(created);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(created));
    }

    // Update API

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable String id,
                                         @RequestBody String body){

        MedicationStatement ms = (MedicationStatement) context
                                    .newJsonParser()
                                    .parseResource(body);

        ms.setId(id);

        MedicationStatement updated = service.update(id, ms);

        service.save(updated);

        return ResponseEntity.ok(
                context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated));
    }
}