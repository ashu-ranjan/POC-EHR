package com.poc.backend.service;

import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.PatientEntity;
import com.poc.backend.mapper.PatientMapper;
import com.poc.backend.repository.PatientRepository;
import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final IGenericClient fhirClient;

    // Constructor Injection
    public PatientService(PatientRepository patientRepository, IGenericClient fhirClient) {
        this.patientRepository = patientRepository;
        this.fhirClient = fhirClient;
    }

    // CREATE PATIENT 

    // Saving Created Patient in DB
    public PatientEntity savePatient(Patient patient){

        // if(!patient.hasId()){
        //     patient.setId(IdGenerator.generatePatientId("PAT", 10, 10));
        // }

        String id = patient.getIdElement().getIdPart();

        String baseUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Patient/")
                    .toUriString();
        String fullUrl = baseUrl + id;

        PatientEntity entity = PatientMapper.patientEntity(patient, fullUrl, "match");
        return patientRepository.save(entity);
    }

    // Created Patient in FHIR
    public Patient createPatient(Patient patient){
        return (Patient) fhirClient
                                .create()
                                .resource(patient)
                                .execute()
                                .getResource();
    }

    // UPDATE PATIENT

    public Patient updatePatient(String id, Patient patient){

        patient.setId(id);

        return (Patient) fhirClient
                    .update()
                    .resource(patient)
                    .execute()
                    .getResource();
    
    
    }
}
