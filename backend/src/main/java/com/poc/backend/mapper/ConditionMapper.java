package com.poc.backend.mapper;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;

import com.poc.backend.entity.ConditionEntity;
import com.poc.backend.entity.EncounterEntity;
import com.poc.backend.entity.PatientEntity;
import com.poc.backend.entity.PractitionerEntity;

import ca.uhn.fhir.context.FhirContext;

public class ConditionMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static ConditionEntity toEntity(Condition condition, String fullUrl, String searchMode){
        ConditionEntity entity = new ConditionEntity();

        // Id
        entity.setId(condition.getIdElement().getIdPart());

        // Clinical Status
        if (condition.hasClinicalStatus()) {
            if (!condition.getClinicalStatus().getCoding().isEmpty()) {
                entity.setClinicalStatus(
                    condition.getClinicalStatus().getCodingFirstRep().getCode()
                );
            } else if (condition.getClinicalStatus().hasText()) {
                entity.setClinicalStatus(
                    condition.getClinicalStatus().getText()
                );
            }
        }


        // Identifier
        if(!condition.getIdentifier().isEmpty()){
            entity.setIdentifier(condition.getIdentifierFirstRep().getValue());
        }

        // Verification Status
        if (condition.hasVerificationStatus()) {
            if (!condition.getVerificationStatus().getCoding().isEmpty()) {
                entity.setVerificationStatus(
                    condition.getVerificationStatus().getCodingFirstRep().getCode()
                );
            } else if (condition.getVerificationStatus().hasText()) {
                entity.setVerificationStatus(
                    condition.getVerificationStatus().getText()
                );
            }
        }

        // Code (Diagnosis)
        if(condition.hasCode()){
            if(condition.getCode().hasText()){
                entity.setCodeText(condition.getCode().getText());
            } else if(!condition.getCode().getCoding().isEmpty()){
                entity.setCodeText(condition.getCode().getCodingFirstRep().getDisplay());
            }
        }

        // severity
        if(condition.hasSeverity()){
            if(!condition.getSeverity().getCoding().isEmpty()){
                entity.setSeverity(condition.getSeverity().getCodingFirstRep().getDisplay());
            } else if(condition.getSeverity().hasText()){
                entity.setSeverity(condition.getSeverity().getText());
            }
        }

        // Category
        if(!condition.getCategory().isEmpty()){
            if(condition.getCategoryFirstRep().hasText()){
                entity.setCategory(condition.getCategoryFirstRep().getText());
            } else if(!condition.getCategoryFirstRep().getCoding().isEmpty()){
                entity.setCategory(condition.getCategoryFirstRep().getCodingFirstRep().getDisplay());
            }
        }

        // Body Site
        if(!condition.getBodySite().isEmpty() && !condition.getBodySiteFirstRep().getCoding().isEmpty()){
            entity.setBodySite(condition.getBodySiteFirstRep().getCodingFirstRep().getDisplay());
        }

        // Onset Date
        if(condition.hasOnsetDateTimeType()){
            entity.setOnsetDate(condition.getOnsetDateTimeType().getValue().toInstant().atOffset(ZoneOffset.UTC));
        }

        // Abatement Date
        if(condition.hasAbatementDateTimeType()){
            entity.setAbatementDate(condition.getAbatementDateTimeType().getValue().toInstant().atOffset(ZoneOffset.UTC));
        }

        // Recorded Date
        if(condition.getRecordedDate() != null){
            // !Problem (needs to be setRecordedDate() -> if error then check here)
            entity.setRecordedTime(condition.getRecordedDate().toInstant().atOffset(ZoneOffset.UTC));
        }

        // Evidence
        if(!condition.getEvidence().isEmpty()){
            String evidence = condition.getEvidence()
                                        .stream()
                                        .flatMap(e -> e.getCode().stream())
                                        .flatMap(c -> c.getCoding().stream())
                                        .map(Coding::getDisplay)
                                        .collect(Collectors.joining(","));

            entity.setEvidence(evidence);
        }

        // Note
        if(!condition.getNote().isEmpty()){
            String note = condition.getNote()
                                    .stream()
                                    .map(n -> n.getText())
                                    .collect(Collectors.joining(","));

            entity.setNote(note);
        }

        // Patient (Subject)
        if(condition.hasSubject() && condition.getSubject().hasReference()){
            String id = extractId(condition.getSubject().getReference());

            PatientEntity patient = new PatientEntity();
            patient.setId(id);
            entity.setPatient(patient);
        }

        // Encounter
        if(condition.hasEncounter() && condition.getEncounter().hasReference()){
            String id = extractId(condition.getEncounter().getReference());

            EncounterEntity encounter = new EncounterEntity();
            encounter.setId(id);
            entity.setEncounter(encounter);
        }

        // Practitioner
        if(condition.hasRecorder() && condition.getRecorder().hasReference()){
            String id = extractId(condition.getRecorder().getReference());

            PractitionerEntity practitioner = new PractitionerEntity();
            practitioner.setId(id);
            entity.setPractitioner(practitioner);
        } else if(condition.hasAsserter() && condition.getAsserter().hasReference()){
            String id = extractId(condition.getAsserter().getReference());

            PractitionerEntity practitioner = new PractitionerEntity();
            practitioner.setId(id);
            entity.setPractitioner(practitioner);
        }

        // Meta
        if(condition.getMeta() != null){
            entity.setVersionId(condition.getMeta().getVersionId());
            
            if(condition.getMeta().getLastUpdated() != null){
                entity.setLastUpdated(condition.getMeta().getLastUpdated().toInstant().atOffset(ZoneOffset.UTC));

            }
        }

        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);

        // Resource Json
        entity.setResourceJson(context.newJsonParser()
                                        .encodeResourceToString(condition));

        return entity;
    }

    // ID Extractor

    private static String extractId(String reference){
        if(reference == null) return null;
        return reference.contains("/")
                        ? reference.substring(reference.lastIndexOf("/") + 1)
                        : reference;
    }

}
