package com.poc.backend.mapper;

import java.time.ZoneOffset;

import org.hl7.fhir.r4.model.Encounter;

import com.poc.backend.entity.EncounterEntity;
import com.poc.backend.entity.LocationEntity;
import com.poc.backend.entity.OrganizationEntity;
import com.poc.backend.entity.PatientEntity;
import com.poc.backend.entity.PractitionerEntity;

import ca.uhn.fhir.context.FhirContext;

public class EncounterMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static EncounterEntity toEntity(Encounter encounter, String fullUrl, String serachMode){
        EncounterEntity entity = new EncounterEntity();

        // Id
        entity.setId(encounter.getIdElement().getIdPart());

        // Identifier
        if(!encounter.getIdentifier().isEmpty()){
            entity.setIdentifier(encounter.getIdentifierFirstRep().getValue());
        }

        // Status
        if(encounter.hasStatus()){
            entity.setStatus(encounter.getStatus().toCode());
        }

        // Patient (Subject)
        if(encounter.hasSubject() && encounter.getSubject().hasReference()){
            String ref = encounter.getSubject().getReference();
            String id = extractId(ref);

            PatientEntity patient = new PatientEntity();

            patient.setId(id);
            entity.setPatient(patient);
        }

        // Practitioner (Participant)
        if(!encounter.getParticipant().isEmpty() 
                        && encounter.getParticipantFirstRep().hasIndividual() 
                        && encounter.getParticipantFirstRep().getIndividual().hasReference()){

            String ref = encounter.getParticipantFirstRep().getIndividual().getReference();
            String id = extractId(ref);

            PractitionerEntity practitioner = new PractitionerEntity();

            practitioner.setId(id);
            entity.setPractitioner(practitioner);
        }

        // Organization (Service Provider)
        if(encounter.hasServiceProvider() && encounter.getServiceProvider().hasReference()){
            String ref = encounter.getServiceProvider().getReference();
            String id = extractId(ref);

            OrganizationEntity organization = new OrganizationEntity();

            organization.setId(id);
            entity.setOrganization(organization);
        }

        // Location
        if(!encounter.getLocation().isEmpty() 
                        && encounter.getLocationFirstRep().hasLocation() 
                        && encounter.getLocationFirstRep().getLocation().hasReference()){

            String ref = encounter.getLocationFirstRep().getLocation().getReference();

            String id = extractId(ref);

            LocationEntity location = new LocationEntity();

            location.setId(id);
            entity.setLocation(location);
        }

        // Period
        if(encounter.hasPeriod()){
            
            // Start Date
            if(encounter.getPeriod().getStart() != null){
                entity.setStartDate(encounter.getPeriod().getStart().toInstant().atOffset(ZoneOffset.UTC));
            }

            // End Date
            if(encounter.getPeriod().getEnd() != null){
                entity.setEndDate(encounter.getPeriod().getEnd().toInstant().atOffset(ZoneOffset.UTC));
            }
        }

        // Description
        if(encounter.hasReasonCode() && !encounter.getReasonCodeFirstRep().getCoding().isEmpty()){
            entity.setDescription(encounter.getReasonCodeFirstRep().getCodingFirstRep().getDisplay());
        }

        // Meta
        if(encounter.getMeta() != null){
            entity.setVersionId(encounter.getMeta().getVersionId());
            if(encounter.getMeta().getLastUpdated() != null){
                entity.setLastUpdated(encounter.getMeta().getLastUpdated().toInstant().atOffset(ZoneOffset.UTC));
            }
        }

        entity.setFullUrl(fullUrl);
        entity.setSearchMode(serachMode);

        // Resource Json 
        entity.setResourceJson(context.newJsonParser().encodeResourceToString(encounter));

        return entity;
    }

    // ID Extractor
    private static String extractId(String reference){
        if(reference == null) return null;
        return reference.contains("/") ? reference.substring(reference.lastIndexOf("/") + 1) : reference;
    }

}
