package com.poc.backend.mapper;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Location;

import com.poc.backend.entity.LocationEntity;
import com.poc.backend.entity.OrganizationEntity;

import ca.uhn.fhir.context.FhirContext;

public class LocationMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static LocationEntity toEntity(Location location, String fullUrl, String searchMode){
        LocationEntity entity = new LocationEntity();

        // Id
        entity.setId(location.getIdElement().getIdPart());

        // Name 
        if(location.hasName()){
            entity.setName(location.getName());
        }

        // Status
        if(location.hasStatus()){
            entity.setStatus(location.getStatus().toCode());
        }

        // Identifier
        if(!location.getIdentifier().isEmpty()){
            entity.setIdentifier(location.getIdentifierFirstRep().getValue());
        }

        // Type
        if(!location.getType().isEmpty() && !location.getTypeFirstRep().getCoding().isEmpty()){
            Coding coding = location.getTypeFirstRep().getCodingFirstRep();

            entity.setTypeCode(coding.getCode());
            entity.setTypeDisplay(coding.getDisplay());
        }

        // Physical Type
        if(location.hasPhysicalType() && !location.getPhysicalType().getCoding().isEmpty()){
            Coding coding = location.getPhysicalType().getCodingFirstRep();

            entity.setPhysicalTypeCode(coding.getCode());
            entity.setPhysicalTypeDisplay(coding.getDisplay());
        }

        // Telecom
        if(!location.getTelecom().isEmpty()){
            String telecom = location.getTelecom()
                                        .stream()
                                        .map(t -> (t.getSystem() != null ? t.getSystem().toCode() : "NA")
                                            + ":" + 
                                            (t.getValue() != null ? t.getValue() : "NA"))
                                        .collect(Collectors.joining(","));
            entity.setTelecom(telecom);
        }

        // Address
        if(location.hasAddress()){
            Address addr = location.getAddress();
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


        // Geo Position
        if(location.hasPosition()){
            if(location.getPosition().hasLatitude()){
                entity.setLatitude(location.getPosition().getLatitude().doubleValue());
            }
            if(location.getPosition().hasLongitude()){
                entity.setLongitude(location.getPosition().getLongitude().doubleValue());
            }
        }

        // Organization Relation
        if(location.hasManagingOrganization()){
            String ref = location.getManagingOrganization().getReference();

            String id = extractId(ref);

            if(id == null && location.getManagingOrganization().hasIdentifier()){
                id = location.getManagingOrganization().getIdentifier().getValue();
            }
            if(id != null){
                OrganizationEntity org = new OrganizationEntity();
                org.setId(id);
                entity.setOrganization(org);
            }
        }

        // Meta
        if(location.getMeta() != null){
            entity.setVersionId(location.getMeta().getVersionId());
            if(location.getMeta().getLastUpdated() != null){
                entity.setLastUpdated(location.getMeta().getLastUpdated().toInstant().atOffset(ZoneOffset.UTC));
            }
        }

        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);

        // Resource Json
        entity.setResourceJson(context.newJsonParser().encodeResourceToString(location));

        return entity;
    }

    // Extract ID helper
    private static String extractId(String reference){
        if(reference == null) return null;
        return reference.contains("/") ? reference.substring(reference.lastIndexOf("/") + 1) : reference;
    }
    

}
