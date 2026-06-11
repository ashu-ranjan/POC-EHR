package com.poc.backend.service;

import org.hl7.fhir.r4.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.OrganizationEntity;
import com.poc.backend.mapper.OrganizationMapper;
import com.poc.backend.repository.OrganizationRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private IGenericClient fhirClient;

    // Create Organization

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

    // Update Organization

    public Organization updateOrganization(String id, Organization organization){

        organization.setId(id);

        return (Organization) fhirClient
                    .update()
                    .resource(organization)
                    .execute()
                    .getResource();
    }

}
