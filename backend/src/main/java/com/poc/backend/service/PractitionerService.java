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

    // Create Practitioner Logic

    public PractitionerEntity savePractitioner(Practitioner practitioner){

        // if(!practitioner.hasId()){
        //     practitioner.setId(IdGenerator.generateDoctorId("DOC", 7, 7));
        // }
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

    // Create Practitioner to FHIR Server

    public Practitioner createPractitioner(Practitioner practitioner){

        // practitioner.setIdElement(null);
        // practitioner.setMeta(null);
        return (Practitioner) fhirClient
        
                                    .create()
                                    .resource(practitioner)
                                    .execute()
                                    .getResource();
    }

    // Update Pratitioner Logic

    public Practitioner updatePractitioner(String id, Practitioner practitioner){
        practitioner.setId(id);

        return (Practitioner) fhirClient
                                    .update()
                                    .resource(practitioner)
                                    .execute()
                                    .getResource();
    }
}
