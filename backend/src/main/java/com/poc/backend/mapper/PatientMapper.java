package com.poc.backend.mapper;

import java.time.ZoneOffset;

import org.hl7.fhir.r4.model.Patient;

import com.poc.backend.entity.PatientEntity;

import ca.uhn.fhir.context.FhirContext;

public class PatientMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static PatientEntity patientEntity(Patient patient, String fullUrl, String searchMode){

        PatientEntity entity = new PatientEntity();

        // Id
        entity.setId(patient.getIdElement().getIdPart());

        // Name
        if(!patient.getName().isEmpty()){
            entity.setFirstName(patient.getNameFirstRep().getGivenAsSingleString());
            entity.setLastName(patient.getNameFirstRep().getFamily());
        }

        // Identifier
        if(!patient.getIdentifier().isEmpty()){
            entity.setIdentifier(patient.getIdentifierFirstRep().getValue());
        }

        // Gender
        entity.setGender(patient.getGender() != null ? patient.getGender().toString() : null);

        // DOB
        entity.setBirthDate(patient.getBirthDate() != null ? patient
                                                                .getBirthDate()
                                                                .toInstant()
                                                                .atZone(java.time.ZoneId.systemDefault())
                                                                .toLocalDate() : null);

        // Telecome
        if (!patient.getTelecom().isEmpty()) {

            patient.getTelecom()
                    .stream()
                    .filter(t -> t.getSystem() != null
                            && t.getSystem().toCode().equals("email"))
                    .findFirst()
                    .ifPresent(t -> entity.setEmail(t.getValue()));
        }

                                                            
        // Meta
        if(patient.getMeta() != null){
            entity.setVersionId(patient.getMeta().getVersionId());
            if(patient.getMeta().getLastUpdated() != null){
            entity.setLastUpdated(patient.getMeta()
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
                                .encodeResourceToString(patient)
        );

        return entity;
    }
}
