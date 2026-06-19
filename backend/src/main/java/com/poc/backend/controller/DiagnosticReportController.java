package com.poc.backend.controller;

import org.hl7.fhir.r4.model.DiagnosticReport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poc.backend.service.DiagnosticReportService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/DiagnosticReport")
public class DiagnosticReportController {

    private final DiagnosticReportService service;
    private final FhirContext context = FhirContext.forR4();

    public DiagnosticReportController(DiagnosticReportService service) {
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body) {

        DiagnosticReport diagnosticReport =
                (DiagnosticReport) context
                        .newJsonParser()
                        .parseResource(body);

        DiagnosticReport created = service.create(diagnosticReport);

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

        DiagnosticReport diagnosticReport =
                (DiagnosticReport) context
                        .newJsonParser()
                        .parseResource(body);

        diagnosticReport.setId(id);

        DiagnosticReport updated = service.update(id, diagnosticReport);

        service.save(updated);

        return ResponseEntity.ok(
                context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated));
    }
}