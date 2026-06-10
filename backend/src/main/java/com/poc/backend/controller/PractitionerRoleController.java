package com.poc.backend.controller;

import org.hl7.fhir.r4.model.PractitionerRole;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poc.backend.service.PractitionerRoleService;

import ca.uhn.fhir.context.FhirContext;

@RestController
public class PractitionerRoleController {

    private final FhirContext context = FhirContext.forR4();

    private PractitionerRoleService practitionerRoleService;

    public PractitionerRoleController(PractitionerRoleService practitionerRoleService){
        this.practitionerRoleService = practitionerRoleService;
    }

    // Create API 

    @PostMapping("/PractitionerRole")
    public String create(@RequestBody String body){
        PractitionerRole role = (PractitionerRole) context
                                        .newJsonParser()
                                        .parseResource(body);

        // Create in FHIR
        PractitionerRole created = practitionerRoleService.creatPractitionerRole(role);

        // Save create in DB
        practitionerRoleService.savePractitionerRole(created);

        return context
                .newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(created);
    }

    // Update API

    @PutMapping("/PractitionerRole/{id}")
    public String update(@PathVariable String id, @RequestBody String body){
        PractitionerRole role = (PractitionerRole) context
                                        .newJsonParser()
                                        .parseResource(body);

        // Update in FHIR
        PractitionerRole updated = practitionerRoleService.updatePractitionerRole(id, role);

        // Save update in DB
        practitionerRoleService.savePractitionerRole(updated);

        return context
                .newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(updated);
    }


}
