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

    @PostMapping("/Patient")
    public String create(@RequestBody String body){

        
        Patient patient = (Patient) FhirContext.forR4()
                    .newJsonParser()
                    .parseResource(body);


        Patient created = patientService.createPatient(patient);
        patientService.savePatient(created);

        return FhirContext.forR4()
                            .newJsonParser()
                            .setPrettyPrint(true)
                            .encodeResourceToString(created);
    }

    @PutMapping("/Patient/{id}")
    public String update(@PathVariable String id, @RequestBody String body){
        Patient patient = (Patient) FhirContext.forR4()
                            .newJsonParser()
                            .parseResource(body);

        Patient updated = patientService.updatePatient(id, patient);
        patientService.savePatient(updated);

        return FhirContext.forR4()
                        .newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(updated);
    }

}
