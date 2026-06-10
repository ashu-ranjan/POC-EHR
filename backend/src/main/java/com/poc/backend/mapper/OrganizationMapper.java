package com.poc.backend.mapper;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Organization;

import com.poc.backend.entity.OrganizationEntity;

import ca.uhn.fhir.context.FhirContext;

public class OrganizationMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static OrganizationEntity toEntity(Organization org, String fullUrl, String searchMode){
        OrganizationEntity entity = new OrganizationEntity();

        // Id
        entity.setId(org.getIdElement().getIdPart());

        // Name
        if(org.hasName()){
            entity.setName(org.getName());
        }

        // Active status
        if(org.hasActive()){
            entity.setActive(org.getActive());
        }

        // Identifier
        if(!org.getIdentifier().isEmpty()){
            entity.setIdentifier(org.getIdentifierFirstRep().getValue());
        }

        // Telecome
        if(!org.getTelecom().isEmpty()){
            String telecom = org.getTelecom()
                                .stream()
                                .map(t -> (t.getSystem() != null ? t.getSystem().toCode() : "NA")
                                            + ":" +
                                        (t.getValue() != null ? t.getValue() : "NA")
                                    )
                                .collect(Collectors.joining(","));
            entity.setTelecom(telecom);
        }

        // Type
        if(!org.getType().isEmpty() && !org.getTypeFirstRep().getCoding().isEmpty()){
            Coding coding = org.getTypeFirstRep().getCodingFirstRep();
            entity.setTypeCode(coding.getCode());
            entity.setTypeDisplay(coding.getDisplay());
        }

        // Address
        if(!org.getAddress().isEmpty()){
            Address addr = org.getAddressFirstRep();
            StringBuilder address = new StringBuilder();

            if(addr.hasLine()){
                address.append(addr.getLine()
                                    .stream()
                                    .map(l -> l.getValue())
                                    .collect(Collectors.joining(","))
                                );
            }
            if(addr.hasCity()) address.append(", ").append(addr.getCity());
            if(addr.hasState()) address.append(", ").append(addr.getState());
            if(addr.hasCountry()) address.append(", ").append(addr.getCountry());
            if(addr.hasPostalCode()) address.append(", ").append(addr.getPostalCode());

            entity.setAddress(address.toString().replaceFirst("^, ", ""));

        }

        // Meta
        if(org.getMeta() != null){
            entity.setVersionId(org.getMeta().getVersionId());
            if(org.getMeta().getLastUpdated() != null){
                entity.setLastUpdated(org.getMeta()
                                        .getLastUpdated()
                                        .toInstant()
                                        .atOffset(ZoneOffset.UTC));
            }
        }

        // Url and Search
        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);

        // Json
        entity.setResourceJson(context.newJsonParser()
                                    .encodeResourceToString(org));

                                    
        return entity;
    }

}
