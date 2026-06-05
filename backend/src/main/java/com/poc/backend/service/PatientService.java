package com.poc.backend.service;

import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.PatientEntity;
import com.poc.backend.mapper.PatientMapper;
import com.poc.backend.repository.PatientRepository;
import com.poc.backend.utility.IdGenerator;
import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private IGenericClient fhirClient;

    // private static final String BASE_URL = "http://localhost:8080/fhir/Patient/" ;


    public PatientEntity save(Patient patient){
        if(!patient.hasId()){
            patient.setId(IdGenerator.generate(10, 16));
        }
        String id = patient.getIdElement().getIdPart();

        String baseUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Patient/")
                    .toUriString();
        String fullUrl = baseUrl + id;

        PatientEntity entity = PatientMapper.patientEntity(patient, fullUrl, "match");
        return patientRepository.save(entity);
    }

    public Patient createPatient(Patient patient){
        return (Patient) fhirClient
                                .create()
                                .resource(patient)
                                .execute()
                                .getResource();
    }
}
