package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Organization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poc.backend.service.OrganizationService;

import ca.uhn.fhir.context.FhirContext;

@RestController
@RequestMapping("/Organization")
public class OrganizationController {

    private final OrganizationService service;
    private final FhirContext context = FhirContext.forR4();

    public OrganizationController(OrganizationService service){
        this.service = service;
    }

    // Create API 

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String body){

        Organization organization = (Organization) context
                                        .newJsonParser()
                                        .parseResource(body);

        Organization created = service.create(organization);

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

        Organization organization = (Organization) context
                                        .newJsonParser()
                                        .parseResource(body);

        organization.setId(id);

        Organization updated = service.update(id, organization);

        service.save(updated);

        return ResponseEntity.ok(
                context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated));
    }
}