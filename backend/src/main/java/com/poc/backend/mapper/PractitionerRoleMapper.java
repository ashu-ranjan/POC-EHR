package com.poc.backend.mapper;

import org.hl7.fhir.r4.model.PractitionerRole;

import com.poc.backend.entity.PractitionerEntity;
import com.poc.backend.entity.PractitionerRoleEntity;

import ca.uhn.fhir.context.FhirContext;

public class PractitionerRoleMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static PractitionerRoleEntity toEntity(PractitionerRole role, PractitionerEntity practitionerEntity){
        PractitionerRoleEntity entity = new PractitionerRoleEntity();

        entity.setId(role.getIdElement().getIdPart());

        entity.setPractitioner(practitionerEntity);
        if(!role.getCode().isEmpty()){
            entity.setRoleCode(role.getCodeFirstRep().getCodingFirstRep().getCode());
            entity.setRoleDisplay(role.getCodeFirstRep().getCodingFirstRep().getDisplay());
        }

        if(role.getPeriod() != null && role.getPeriod().getStart() != null){
            entity.setStartDate(role.getPeriod().getStart().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate());
        }

        entity.setResourceJson(context.newJsonParser().encodeResourceToString(role));
        return entity;
    }

}
