package com.poc.backend.mapper;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.PractitionerRole;

import com.poc.backend.entity.LocationEntity;
import com.poc.backend.entity.OrganizationEntity;
import com.poc.backend.entity.PractitionerEntity;
import com.poc.backend.entity.PractitionerRoleEntity;

import ca.uhn.fhir.context.FhirContext;

public class PractitionerRoleMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static PractitionerRoleEntity toEntity(PractitionerRole role, String fullUrl, String searchMode){

        PractitionerRoleEntity entity = new PractitionerRoleEntity();

        // Id
        entity.setId(role.getIdElement().getIdPart());

        // Status
        if(role.hasActive()){
            entity.setActive(role.getActive());
        }

        // Practitioner

        if(role.hasPractitioner()){
            String ref = role.getPractitioner().getReference();
            String id = extractId(ref);

            PractitionerEntity pEntity = new PractitionerEntity();
            pEntity.setId(id);
            entity.setPractitioner(pEntity);
        }

        // Organization

        if(role.hasOrganization()){
            String ref = role.getOrganization().getReference();
            if(ref != null){
                String id = extractId(ref);

                OrganizationEntity oEntity = new OrganizationEntity();
                oEntity.setId(id);
                entity.setOrganization(oEntity);
            }
        }

        // Location

        if(role.hasLocation()){
            List<LocationEntity> locations = new ArrayList<>();
            for(org.hl7.fhir.r4.model.Reference refObj : role.getLocation()){
                if(refObj.getReference() != null){
                    String id = extractId(refObj.getReference());
                    LocationEntity lEntity = new LocationEntity();

                    lEntity.setId(id);
                    locations.add(lEntity);
                }
            }
            entity.setLocation(locations);
        }

        // Role Code

        if(!role.getCode().isEmpty() && !role.getCodeFirstRep().getCoding().isEmpty()){
            Coding coding = role.getCodeFirstRep().getCodingFirstRep();
            entity.setRoleCode(coding.getCode());
            entity.setRoleDisplay(coding.getDisplay());
        }

        // Speciality

        if(!role.getSpecialty().isEmpty() && !role.getSpecialtyFirstRep().getCoding().isEmpty()){
            Coding coding = role.getSpecialtyFirstRep().getCodingFirstRep();
            entity.setSpecialty(coding.getDisplay());
        }

        // Period

        if(role.hasPeriod()){
            if(role.getPeriod().getStart() != null){
                entity.setPeriodStart(role.getPeriod().getStart().toInstant().atOffset(ZoneOffset.UTC));
            }
            if(role.getPeriod().getEnd() != null){
                entity.setPeriodEnd(role.getPeriod().getEnd().toInstant().atOffset(ZoneOffset.UTC));
            }
        }

        // Telecom
        
        
        if (!role.getTelecom().isEmpty()) {
            String telecom = role.getTelecom()
                    .stream()
                    .map(t -> (t.getSystem() != null ? t.getSystem() : "NA") 
                            + ":" + 
                            (t.getValue() != null ? t.getValue() : "NA"))
                    .collect(Collectors.joining(","));

            entity.setTelecom(telecom);
        }


        // Meta

        if(role.getMeta() != null){
            entity.setVersionId(role.getMeta().getVersionId());
            if(role.getMeta().getLastUpdated() != null){
                entity.setLastUpdated(role.getMeta().getLastUpdated().toInstant().atOffset(ZoneOffset.UTC));
            }
        }

        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);

        // Json
        entity.setResourceJson(context
                    .newJsonParser()
                    .encodeResourceToString(role));
                    
        return entity;
    }

    // Extract ID method 

    private static String extractId(String reference){

        if(reference == null ) return null;
        return reference.contains("/") ? reference.substring(reference.lastIndexOf("/") + 1) : reference;
    }

}
