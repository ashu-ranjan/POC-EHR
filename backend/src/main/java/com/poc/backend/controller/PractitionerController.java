package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poc.backend.service.PractitionerService;

import ca.uhn.fhir.context.FhirContext;

@RestController
public class PractitionerController {

    private final FhirContext context = FhirContext.forR4();

    private PractitionerService practitionerService;
    public PractitionerController(PractitionerService practitionerService){
        this.practitionerService = practitionerService;
    }

    // Create Controller 

    @PostMapping("/Practitioner")
    public String create(@RequestBody String body){
        Practitioner practitioner = (Practitioner) context
                                        .newJsonParser()
                                        .parseResource(body);
        Practitioner created = practitionerService.createPractitioner(practitioner);
        practitionerService.savePractitioner(created);

        return context
                .newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(created);
    }

    // Update Controller

    @PutMapping("/Practitioner/{id}")
    public String update(@PathVariable String id, @RequestBody String body){
        Practitioner practitioner = (Practitioner) context
                                        .newJsonParser()
                                        .parseResource(body);

        Practitioner updated = practitionerService.updatePractitioner(id, practitioner);
        practitionerService.savePractitioner(updated);

        return context
                .newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(updated);
    }


}
