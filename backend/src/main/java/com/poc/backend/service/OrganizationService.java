package com.poc.backend.service;

import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.OrganizationEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.OrganizationMapper;
import com.poc.backend.repository.OrganizationRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class OrganizationService {

    private final OrganizationRepository repository;
    private final IGenericClient fhirClient;

    public OrganizationService(OrganizationRepository repository,
                               IGenericClient fhirClient) {
        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE ORGANIZATION

    // Create Organization to FHIR
    public Organization create(Organization organization){

        // validation
        if (!organization.hasName()) {
            throw new BadRequestException("Organization name is required.");
        }

        // identifier generation
        if(organization.getIdentifier().isEmpty()){
            Identifier identifier = new Identifier();
            identifier.setSystem("http://hospital-system/organization");
            identifier.setValue(
                    IdGenerator.generateIdentifier("ORG-", 4, 6)
            );

            organization.addIdentifier(identifier);
        }

        // core creation
        try {
            return (Organization) fhirClient
                    .create()
                    .resource(organization)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create Organization.");
        }
    }

    // Save Organization to DB
    public OrganizationEntity save(Organization organization){

        try {
            String id = organization.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Organization/")
                    .toUriString() + id;

            OrganizationEntity entity = OrganizationMapper.toEntity(organization, fullUrl, "match");
            
            if (entity.getIdentifier() == null) {
                repository.findById(id)
                        .ifPresent(existing -> entity.setIdentifier(existing.getIdentifier()));
            }
            return repository.save(entity);

        } catch (Exception e) {
            throw new DatabaseException("Failed to save Organization.");
        }
    }

    // UPDATE ORGANIZATION
    
    public Organization update(String id, Organization organization){

        // validation
        if(id == null || id.isEmpty()){
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            Organization existing =
                    (Organization) fhirClient
                            .read()
                            .resource(Organization.class)
                            .withId(id)
                            .execute();

            // ALWAYS preserve identifier (including system)
            organization.setIdentifier(existing.getIdentifier());

            organization.setId(id);

            return (Organization) fhirClient
                    .update()
                    .resource(organization)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update Organization.");
        }
    }
}
