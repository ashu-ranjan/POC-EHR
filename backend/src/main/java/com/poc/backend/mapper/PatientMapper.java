package com.poc.backend.mapper;

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

        // Gender
        entity.setGender(patient.getGender() != null ? patient.getGender().toString() : null);

        // DOB
        entity.setBirthDate(patient.getBirthDate() != null ? patient
                                                                .getBirthDate()
                                                                .toInstant()
                                                                .atZone(java.time.ZoneId.systemDefault())
                                                                .toLocalDate() : null);
                                                            
        // Meta
        if(patient.getMeta() != null){
            entity.setLastUpdated(patient.getMeta().getLastUpdated().toInstant().atOffset(java.time.ZoneOffset.UTC));
            entity.setSource(patient.getMeta().getSource());
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
