package com.poc.backend.service;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.PractitionerRoleEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.PractitionerRoleMapper;
import com.poc.backend.repository.PractitionerRoleRepository;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class PractitionerRoleService {

    private final PractitionerRoleRepository repository;
    private final IGenericClient fhirClient;

    public PractitionerRoleService(PractitionerRoleRepository repository,
                                   IGenericClient fhirClient) {
        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE PRACTITIONER ROLE

    // Create PractitionerRole to FHIR
    public PractitionerRole create(PractitionerRole role){

        // validation
        if(!role.hasPractitioner() || !role.hasOrganization()){
            throw new BadRequestException("Practitioner and Organization references are required.");
        }

        // core creation
        try {
            return (PractitionerRole) fhirClient
                    .create()
                    .resource(role)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create PractitionerRole.");
        }
    }

    // Save PractitionerRole to DB
    public PractitionerRoleEntity save(PractitionerRole role){

        try {
            String id = role.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/PractitionerRole/")
                    .toUriString() + id;

            return repository.save(
                    PractitionerRoleMapper.toEntity(role, fullUrl, "match"));

        } catch (Exception e) {
            throw new DatabaseException("Failed to save PractitionerRole.");
        }
    }

    // UPDATE PRACTITIONER ROLE

    public PractitionerRole update(String id, PractitionerRole role){

        // validation
        if(id == null || id.isEmpty()){
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            role.setId(id);

            return (PractitionerRole) fhirClient
                    .update()
                    .resource(role)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update PractitionerRole.");
        }
    }
}


