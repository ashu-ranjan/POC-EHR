package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Organization;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poc.backend.service.OrganizationService;

import ca.uhn.fhir.context.FhirContext;

@RestController
public class OrganizationController {

    private final FhirContext context = FhirContext.forR4();
    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService){
        this.organizationService = organizationService;
    }

    // Create API

    @PostMapping("/Organization")
    public String create(@RequestBody String body){
        Organization organization = (Organization) context
                                        .newJsonParser()
                                        .parseResource(body);
                
        // Create in FHIR
        Organization created = organizationService.createOrganization(organization);

        // Save create in DB
        organizationService.saveOrganization(created);

        return context.newJsonParser()
                    .setPrettyPrint(true)
                    .encodeResourceToString(created);
    }

    // Update API

    @PutMapping("/Organization/{id}")
    public String update(@PathVariable String id, @RequestBody String body){
        Organization organization = (Organization) context
                                        .newJsonParser()
                                        .parseResource(body);
        // Update in FHIR
        Organization updated = organizationService.updateOrganization(id, organization);

        // Save update in DB
        organizationService.saveOrganization(updated);

        return context.newJsonParser()
                    .setPrettyPrint(true)
                    .encodeResourceToString(updated);
    }

}
