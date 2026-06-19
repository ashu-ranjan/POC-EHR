package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poc.backend.service.PatientService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/Patient")
public class PatientController {

    private final PatientService service;
    private final FhirContext context = FhirContext.forR4();

    public PatientController(PatientService service){
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body){

        Patient patient = (Patient) context
                    .newJsonParser()
                    .parseResource(body);

        Patient created = service.create(patient);

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

        Patient patient = (Patient) context
                            .newJsonParser()
                            .parseResource(body);

        patient.setId(id);

        Patient updated = service.update(id, patient);

        service.save(updated);

        return ResponseEntity.ok(
                context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated));
    }
}
