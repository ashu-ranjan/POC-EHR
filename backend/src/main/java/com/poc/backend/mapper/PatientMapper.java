package com.poc.backend.mapper;

import org.hl7.fhir.r4.model.Patient;

import com.poc.backend.entity.PatientEntity;

import ca.uhn.fhir.context.FhirContext;

public class PatientMapper {

    public static PatientEntity patientEntity(Patient patient, String fullUrl, String searchMode){

        PatientEntity entity = new PatientEntity();

        entity.setId(patient.getIdElement().getIdPart());

        if(!patient.getName().isEmpty()){
            entity.setFirstName(patient.getNameFirstRep().getGivenAsSingleString());
            entity.setLastName(patient.getNameFirstRep().getFamily());
        }

        entity.setGender(patient.getGender() != null ? patient.getGender().toString() : null);

        entity.setBirthDate(patient.getBirthDate() != null ? patient
                                                                .getBirthDate()
                                                                .toInstant()
                                                                .atZone(java.time.ZoneId.systemDefault())
                                                                .toLocalDate() : null);
        if(patient.getMeta() != null){
            entity.setLastUpdated(patient.getMeta().getLastUpdated().toInstant().atOffset(java.time.ZoneOffset.UTC));
            entity.setSource(patient.getMeta().getSource());
        }

        String json = FhirContext.forR4()
                    .newJsonParser()
                    .encodeResourceToString(patient);

        entity.setResourceJson(json);

        entity.setFullUrl(fullUrl);
        
        entity.setSearchMode(searchMode);

        return entity;
    }
}
