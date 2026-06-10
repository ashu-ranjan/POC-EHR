package com.poc.backend.mapper;

import java.time.ZoneOffset;

import org.hl7.fhir.r4.model.Practitioner;

import com.poc.backend.entity.PractitionerEntity;

import ca.uhn.fhir.context.FhirContext;

public class PractitionerMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static PractitionerEntity toEntity(Practitioner practitioner, String fullUrl, String searchMode){

        PractitionerEntity entity = new PractitionerEntity();

        // Id
        entity.setId(practitioner.getIdElement().getIdPart());

        // Name
        if(!practitioner.getName().isEmpty()){
            entity.setFirstName(practitioner.getNameFirstRep().getGivenAsSingleString());
            entity.setLastName(practitioner.getNameFirstRep().getFamily());
        }

        // Status
        entity.setActive(practitioner.getActive());

        // Telecom
        if(!practitioner.getTelecom().isEmpty()){
            entity.setEmail(practitioner.getTelecomFirstRep().getValue());
        }
        if(!practitioner.getIdentifier().isEmpty()){
            entity.setIdentifier(practitioner.getIdentifierFirstRep().getValue());
        }

        // Meta
        if(practitioner.getMeta() != null){
            entity.setVersionId(practitioner.getMeta().getVersionId());
            if(practitioner.getMeta().getLastUpdated() != null){
            entity.setLastUpdated(practitioner.getMeta()
                                    .getLastUpdated()
                                    .toInstant()
                                    .atOffset(ZoneOffset.UTC));                  
            }
        }
        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);
        
        // Json
        entity.setResourceJson(context
                        .newJsonParser()
                        .encodeResourceToString(practitioner));
                        
        return entity;
    }

}
