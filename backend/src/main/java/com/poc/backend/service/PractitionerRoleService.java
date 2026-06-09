package com.poc.backend.service;

import org.hl7.fhir.r4.model.PractitionerRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.backend.entity.PractitionerEntity;
import com.poc.backend.entity.PractitionerRoleEntity;
import com.poc.backend.mapper.PractitionerRoleMapper;
import com.poc.backend.repository.PractitionerRepository;
import com.poc.backend.repository.PractitionerRoleRepository;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class PractitionerRoleService {

    @Autowired
    private PractitionerRoleRepository practitionerRoleRepository;

    @Autowired
    private PractitionerRepository practitionerRepository;

    @Autowired
    private IGenericClient fhirClient;

    // Create Practitioner Role

    public PractitionerRole create(PractitionerRole role){
        PractitionerRole created = (PractitionerRole) fhirClient
                                        .create()
                                        .resource(role)
                                        .execute()
                                        .getResource();
        String ref = created.getPractitioner().getReference();
        String practitionerId = ref.split("/")[1];

        PractitionerEntity practitionerEntity = practitionerRepository.findById(practitionerId)
                                    .orElseThrow(()-> new RuntimeException("Practitioner not Found!"));
        
        PractitionerRoleEntity entity = PractitionerRoleMapper.toEntity(created, practitionerEntity);

        practitionerRoleRepository.save(entity);

        return created;
    }

    // Update Practitioner Role

    public PractitionerRole update(String id, PractitionerRole role){
        role.setId(id);

        PractitionerRole updated = (PractitionerRole) fhirClient
                                        .update()
                                        .resource(role)
                                        .execute()
                                        .getResource();

        String ref = updated.getPractitioner().getReference();
        String practitionerId = ref.split("/")[1];

        PractitionerEntity practitionerEntity = practitionerRepository.findById(practitionerId)
                            .orElseThrow(()-> new RuntimeException("Practitioner not Found!"));
        
        PractitionerRoleEntity entity = PractitionerRoleMapper.toEntity(updated, practitionerEntity);
        practitionerRoleRepository.save(entity);

        return updated;
    }

}
