package com.poc.backend.mapper;

import java.time.ZoneOffset;

import org.hl7.fhir.r4.model.Practitioner;

import com.poc.backend.entity.PractitionerEntity;

import ca.uhn.fhir.context.FhirContext;

public class PractitionerMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static PractitionerEntity toEntity(Practitioner practitioner, String fullUrl, String searchMode){
        PractitionerEntity entity = new PractitionerEntity();

        entity.setId(practitioner.getIdElement().getIdPart());
        if(!practitioner.getName().isEmpty()){
            entity.setFirstName(practitioner.getNameFirstRep().getGivenAsSingleString());
            entity.setLastName(practitioner.getNameFirstRep().getFamily());
        }
        entity.setActive(practitioner.getActive());
        if(!practitioner.getTelecom().isEmpty()){
            entity.setEmail(practitioner.getTelecomFirstRep().getValue());
        }
        if(!practitioner.getIdentifier().isEmpty()){
            entity.setIdentifier(practitioner.getIdentifierFirstRep().getValue());
        }
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
        
        entity.setResourceJson(context.newJsonParser().encodeResourceToString(practitioner));
        return entity;
    }

}
