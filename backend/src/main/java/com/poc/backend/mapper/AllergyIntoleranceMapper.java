package com.poc.backend.mapper;

import java.time.ZoneOffset;

import com.poc.backend.entity.AllergyIntoleranceEntity;
import com.poc.backend.entity.EncounterEntity;
import com.poc.backend.entity.PatientEntity;
import com.poc.backend.entity.PractitionerEntity;

import ca.uhn.fhir.context.FhirContext;

public class AllergyIntoleranceMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static AllergyIntoleranceEntity toEntity(
            org.hl7.fhir.r4.model.AllergyIntolerance a,
            String fullUrl,
            String searchMode) {

        AllergyIntoleranceEntity entity = new AllergyIntoleranceEntity();

        // Id
        entity.setId(a.getIdElement().getIdPart());

        // Type
        if (a.hasType()) {
            entity.setType(a.getType().toCode());
        }

        // Clinical Status
        if (a.hasClinicalStatus()) {
            if (!a.getClinicalStatus().getCoding().isEmpty()) {
                entity.setClinicalStatus(
                    a.getClinicalStatus().getCodingFirstRep().getCode()
                );
            } else if (a.getClinicalStatus().hasText()) {
                entity.setClinicalStatus(
                    a.getClinicalStatus().getText()
                );
            }
        }
        
        // Verification Status
        if (a.hasVerificationStatus()) {
            if (!a.getVerificationStatus().getCoding().isEmpty()) {
                entity.setVerificationStatus(
                    a.getVerificationStatus().getCodingFirstRep().getCode()
                );
            } else if (a.getVerificationStatus().hasText()) {
                entity.setVerificationStatus(
                    a.getVerificationStatus().getText()
                );
            }
        }


        // Code
        if (a.hasCode()) {
            if (a.getCode().hasText()) {
                entity.setCode(a.getCode().getText());
            } else {
                entity.setCode(
                        a.getCode().getCodingFirstRep().getDisplay()
                );
            }
        }

        // Record date
        if (a.hasRecordedDate()) {
            entity.setRecordedDate(
                    a.getRecordedDate().toInstant().atOffset(ZoneOffset.UTC)
            );
        }

        // patient
        if (a.hasPatient()) {
            PatientEntity p = new PatientEntity();
            p.setId(extractId(a.getPatient().getReference()));
            entity.setPatient(p);
        }

        // practitioner
        if (a.hasRecorder()) {
            PractitionerEntity pr = new PractitionerEntity();
            pr.setId(extractId(a.getRecorder().getReference()));
            entity.setPractitioner(pr);
        }

        // encounter
        if (a.hasEncounter()) {
            EncounterEntity enc = new EncounterEntity();
            enc.setId(extractId(a.getEncounter().getReference()));
            entity.setEncounter(enc);
        }

        // Reaction
        if (!a.getReaction().isEmpty()) {

            var r = a.getReactionFirstRep();

            if (r.hasDescription()) {
                entity.setReaction(r.getDescription());
            } 
            else if (!r.getManifestation().isEmpty()) {
                entity.setReaction(
                    r.getManifestationFirstRep().getText()
                );
            }

            if (r.hasSeverity()) {
                entity.setSeverity(r.getSeverity().toCode());
            }
        }

        // Meta
        if (a.getMeta() != null) {
            entity.setVersionId(a.getMeta().getVersionId());

            if (a.getMeta().getLastUpdated() != null) {
                entity.setLastUpdated(
                        a.getMeta().getLastUpdated()
                                .toInstant()
                                .atOffset(ZoneOffset.UTC)
                );
            }
        }

        // Identifier
        if(!a.getIdentifier().isEmpty()){
            entity.setIdentifier(a.getIdentifierFirstRep().getValue());
        }

        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);

        entity.setResourceJson(
                context.newJsonParser().encodeResourceToString(a)
        );

        return entity;
    }

    private static String extractId(String ref) {
        return ref.substring(ref.lastIndexOf("/") + 1);
    }
}
