package com.poc.backend.service;

import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.PractitionerEntity;
import com.poc.backend.mapper.PractitionerMapper;
import com.poc.backend.repository.PractitionerRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class PractitionerService {

    @Autowired
    private PractitionerRepository practitionerRepository;

    @Autowired
    private IGenericClient fhirClient;

    // CREATE PRACTITIONER

    // Saving created practitioner in DB
    public PractitionerEntity savePractitioner(Practitioner practitioner){

        String id = practitioner.getIdElement().getIdPart();
        
        if(practitioner.getIdentifier().isEmpty()){

            String identifierValue = IdGenerator.generateDocIdentifier("PRAC-", 4, 6);

            practitioner.addIdentifier()
                        .setSystem("http://hospital-system/practitioner")
                        .setValue(identifierValue);
        }


        String baseUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Practitioner/")
                    .toUriString();
        String fullUrl = baseUrl + id;

        PractitionerEntity entity = PractitionerMapper.toEntity(practitioner, fullUrl, "match");
        return practitionerRepository.save(entity);
    }

    // Created practitioner in FHIR
    public Practitioner createPractitioner(Practitioner practitioner){

        return (Practitioner) fhirClient
                                    .create()
                                    .resource(practitioner)
                                    .execute()
                                    .getResource();
    }

    // UPDATE PRACTITIONER

    public Practitioner updatePractitioner(String id, Practitioner practitioner){
        practitioner.setId(id);

        return (Practitioner) fhirClient
                                    .update()
                                    .resource(practitioner)
                                    .execute()
                                    .getResource();
    }
}
