package com.poc.backend.controller;

import org.hl7.fhir.r4.model.Patient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.poc.backend.service.PatientService;

import ca.uhn.fhir.context.FhirContext;

@RestController
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }

    // Create API

    @PostMapping("/Patient")
    public String create(@RequestBody String body){

        
        Patient patient = (Patient) FhirContext.forR4()
                    .newJsonParser()
                    .parseResource(body);


        // Create in FHIR
        Patient created = patientService.createPatient(patient);

        // Save create in DB
        patientService.savePatient(created);

        return FhirContext.forR4()
                            .newJsonParser()
                            .setPrettyPrint(true)
                            .encodeResourceToString(created);
    }

    // Update API

    @PutMapping("/Patient/{id}")
    public String update(@PathVariable String id, @RequestBody String body){
        Patient patient = (Patient) FhirContext.forR4()
                            .newJsonParser()
                            .parseResource(body);

        // Update in FHIR
        Patient updated = patientService.updatePatient(id, patient);

        // Save update in DB
        patientService.savePatient(updated);

        return FhirContext.forR4()
                        .newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated);
    }

}
