package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poc.backend.service.ProcedureService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/Procedure")
public class ProcedureController {

    private final ProcedureService service;
    private final FhirContext context = FhirContext.forR4();

    public ProcedureController(ProcedureService service) {
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body) {

        Procedure procedure = (Procedure) context
                .newJsonParser()
                .parseResource(body);

        Procedure created = service.create(procedure);

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

        Procedure procedure = (Procedure) context
                .newJsonParser()
                .parseResource(body);

        procedure.setId(id);

        Procedure updated = service.update(id, procedure);

        service.save(updated);

        return ResponseEntity.ok(
                context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated));
    }
}
