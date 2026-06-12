package com.poc.backend.service;

import org.hl7.fhir.r4.model.Organization;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.OrganizationEntity;
import com.poc.backend.mapper.OrganizationMapper;
import com.poc.backend.repository.OrganizationRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final IGenericClient fhirClient;

    // Constructor Injection
    public OrganizationService(OrganizationRepository organizationRepository, IGenericClient fhirClient) {
        this.organizationRepository = organizationRepository;
        this.fhirClient = fhirClient;
    }

    // CREATE ORGANIZATION

    public OrganizationEntity saveOrganization(Organization organization){
        
        String id = organization.getIdElement().getIdPart();

        if(organization.getIdentifier().isEmpty()){

            String identifierValue = IdGenerator.generateOrgIdentifier("ORG-", 4, 6);

            organization.addIdentifier()
                        .setSystem("http://hospital-system/organization")
                        .setValue(identifierValue);
        }

        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/Organization/")
                .toUriString();
        String fullUrl = baseUrl + id;

        OrganizationEntity entity = OrganizationMapper.toEntity(organization, fullUrl, "match");

        return organizationRepository.save(entity);

    }

    public Organization createOrganization(Organization organization){

        // organization.setIdElement(null); When needed active this

        return (Organization) fhirClient
                    .create()
                    .resource(organization)
                    .execute()
                    .getResource();
    }

    // UPDATE ORGANIZATION

    public Organization updateOrganization(String id, Organization organization){

        organization.setId(id);

        return (Organization) fhirClient
                    .update()
                    .resource(organization)
                    .execute()
                    .getResource();
    }

}
