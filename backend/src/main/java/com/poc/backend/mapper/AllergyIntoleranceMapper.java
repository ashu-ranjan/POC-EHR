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

        entity.setId(a.getIdElement().getIdPart());

        if (a.hasType()) {
            entity.setType(a.getType().toCode());
        }

        if (a.hasClinicalStatus()) {
            entity.setClinicalStatus(
                    a.getClinicalStatus().getCodingFirstRep().getCode()
            );
        }

        if (a.hasVerificationStatus()) {
            entity.setVerificationStatus(
                    a.getVerificationStatus().getCodingFirstRep().getCode()
            );
        }

        if (a.hasCode()) {
            if (a.getCode().hasText()) {
                entity.setCode(a.getCode().getText());
            } else {
                entity.setCode(
                        a.getCode().getCodingFirstRep().getDisplay()
                );
            }
        }

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

        // reaction
        if (!a.getReaction().isEmpty()) {

            var r = a.getReactionFirstRep();

            if (!r.getManifestation().isEmpty()) {
                entity.setReaction(
                        r.getManifestationFirstRep()
                         .getText()
                );
            }

            if (r.hasSeverity()) {
                entity.setSeverity(r.getSeverity().toCode());
            }
        }

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
