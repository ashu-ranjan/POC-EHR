package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Appointment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poc.backend.service.AppointmentService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/Appointment")
public class AppointmentController {

    private final AppointmentService service;
    private final FhirContext context = FhirContext.forR4();

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body) {

        Appointment appt =
                (Appointment) context
                        .newJsonParser()
                        .parseResource(body);

        Appointment created = service.create(appt);

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

        Appointment appt =
                (Appointment) context
                        .newJsonParser()
                        .parseResource(body);
    
    appt.setId(id);

        Appointment updated = service.update(id, appt);

        service.save(updated);

        return ResponseEntity.ok(
                context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated));
    }
}