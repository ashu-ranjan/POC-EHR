package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Condition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poc.backend.service.ConditionService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/Condition")
public class ConditionController {

    private final ConditionService service;
    private final FhirContext context = FhirContext.forR4();

    public ConditionController(ConditionService service) {
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body) {

        Condition condition =
                (Condition) context
                        .newJsonParser()
                        .parseResource(body);

        Condition created = service.create(condition);

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

        Condition condition =
                (Condition) context
                        .newJsonParser()
                        .parseResource(body);

        condition.setId(id);

        Condition updated = service.update(id, condition);

        service.save(updated);

        return ResponseEntity.ok(
                context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated));
    }
}