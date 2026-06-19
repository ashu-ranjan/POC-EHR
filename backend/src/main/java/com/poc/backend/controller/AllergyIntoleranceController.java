package com.poc.backend.controller;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.backend.service.AllergyIntoleranceService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/AllergyIntolerance")
public class AllergyIntoleranceController {

    private final AllergyIntoleranceService service;
    private final FhirContext context = FhirContext.forR4();

    public AllergyIntoleranceController(AllergyIntoleranceService service) {
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body) {
        AllergyIntolerance a =
                    (AllergyIntolerance) context
                            .newJsonParser()
                            .parseResource(body);

            AllergyIntolerance created = service.create(a);
            service.save(created);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(context.newJsonParser()
                            .setPrettyPrint(true)
                            .encodeResourceToString(created));

    }

    // Update API

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable String id,
                                         @RequestBody String body) {
        AllergyIntolerance a =
                    (AllergyIntolerance) context
                            .newJsonParser()
                            .parseResource(body);

            a.setId(id);

            AllergyIntolerance updated = service.update(id, a);

            service.save(updated);

            return ResponseEntity.ok(
                    context.newJsonParser()
                            .setPrettyPrint(true)
                            .encodeResourceToString(updated));

    }
}

