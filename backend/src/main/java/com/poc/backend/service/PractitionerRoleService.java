package com.poc.backend.service;

import org.hl7.fhir.r4.model.PractitionerRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.PractitionerRoleEntity;
import com.poc.backend.mapper.PractitionerRoleMapper;
import com.poc.backend.repository.PractitionerRoleRepository;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class PractitionerRoleService {

    @Autowired
    private PractitionerRoleRepository practitionerRoleRepository;

    @Autowired
    private IGenericClient fhirClient;

    // CREATE PRACTITIONER ROLE

    // Saving created practitioner role in DB 
    public PractitionerRoleEntity savePractitionerRole(PractitionerRole role){
        
        String id = role.getIdElement().getIdPart();

        String baseUrl = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/PractitionerRole/")
                            .toUriString();

        String fullUrl = baseUrl + id;

        PractitionerRoleEntity entity = PractitionerRoleMapper.toEntity(role, fullUrl, "match");

        return practitionerRoleRepository.save(entity);
    }

    // Created practitioner role in FHIR
    public PractitionerRole creatPractitionerRole(PractitionerRole role){
        return (PractitionerRole) fhirClient
                    .create()
                    .resource(role)
                    .execute()
                    .getResource();
    }

    // UPDATE PRACTITIONER ROLE

    public PractitionerRole updatePractitionerRole(String id, PractitionerRole role){

        role.setId(id);

        return (PractitionerRole) fhirClient
                    .update()
                    .resource(role)
                    .execute()
                    .getResource();

    }

}
