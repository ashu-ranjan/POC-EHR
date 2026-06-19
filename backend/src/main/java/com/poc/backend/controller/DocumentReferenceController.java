package com.poc.backend.controller;

import org.hl7.fhir.r4.model.DocumentReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poc.backend.service.DocumentReferenceService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/DocumentReference")
public class DocumentReferenceController {

    private final DocumentReferenceService service;
    private final FhirContext context = FhirContext.forR4();

    public DocumentReferenceController(DocumentReferenceService service) {
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body) {

        DocumentReference doc =
                (DocumentReference) context
                        .newJsonParser()
                        .parseResource(body);

        DocumentReference created = service.create(doc);

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

        DocumentReference doc =
                (DocumentReference) context
                        .newJsonParser()
                        .parseResource(body);

        doc.setId(id);

        DocumentReference updated = service.update(id, doc);

        service.save(updated);

        return ResponseEntity.ok(
                context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated));
    }
}