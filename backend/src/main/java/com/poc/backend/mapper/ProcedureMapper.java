package com.poc.backend.mapper;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.Procedure;

import com.poc.backend.entity.EncounterEntity;
import com.poc.backend.entity.PatientEntity;
import com.poc.backend.entity.ProcedureEntity;

import ca.uhn.fhir.context.FhirContext;

public class ProcedureMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static ProcedureEntity toEntity(
            Procedure procedure,
            String fullUrl,
            String searchMode) {

        ProcedureEntity entity = new ProcedureEntity();

        // ID
        entity.setId(procedure.getIdElement().getIdPart());

        // Status
        if (procedure.hasStatus()) {
            entity.setStatus(procedure.getStatus().toCode());
        }

        // Identifier
        if (!procedure.getIdentifier().isEmpty()) {
            String identifier = procedure.getIdentifier()
                    .stream()
                    .map(i -> i.getValue())
                    .collect(Collectors.joining(" | "));
            entity.setIdentifier(identifier);
        }

        // Procedure Name
        if (procedure.hasCode()) {
            if (procedure.getCode().hasText()) {
                entity.setProcedureText(procedure.getCode().getText());
            } else if (!procedure.getCode().getCoding().isEmpty()) {
                entity.setProcedureText(
                        procedure.getCode()
                                 .getCodingFirstRep()
                                 .getDisplay()
                );
            }
        }

        // Category
        if (procedure.hasCategory()) {
            if (procedure.getCategory().hasText()) {
                entity.setCategory(procedure.getCategory().getText());
            } else if (!procedure.getCategory().getCoding().isEmpty()) {
                entity.setCategory(
                        procedure.getCategory()
                                 .getCodingFirstRep()
                                 .getDisplay()
                );
            }
        }

        // Performed Date
        if (procedure.hasPerformedDateTimeType()) {
            entity.setPerformedDate(
                    procedure.getPerformedDateTimeType()
                             .getValue()
                             .toInstant()
                             .atOffset(ZoneOffset.UTC)
            );
        }

        // Body Site
        if (!procedure.getBodySite().isEmpty()) {
            if (procedure.getBodySiteFirstRep().hasText()) {
                entity.setBodySite(procedure.getBodySiteFirstRep().getText());
            } else if (!procedure.getBodySiteFirstRep().getCoding().isEmpty()) {
                entity.setBodySite(
                        procedure.getBodySiteFirstRep()
                                 .getCodingFirstRep()
                                 .getDisplay()
                );
            }
        }

        // Patient
        if (procedure.hasSubject()
                && procedure.getSubject().hasReference()) {

            String patientId = extractId(
                    procedure.getSubject().getReference()
            );

            PatientEntity patient = new PatientEntity();
            patient.setId(patientId);

            entity.setPatient(patient);
        }

        // Encounter
        if (procedure.hasEncounter()
                && procedure.getEncounter().hasReference()) {

            String encId = extractId(
                    procedure.getEncounter().getReference()
            );

            EncounterEntity encounter = new EncounterEntity();
            encounter.setId(encId);

            entity.setEncounter(encounter);
        }

        // Meta
        if (procedure.getMeta() != null) {

            entity.setVersionId(procedure.getMeta().getVersionId());

            if (procedure.getMeta().getLastUpdated() != null) {
                entity.setLastUpdated(
                        procedure.getMeta()
                                 .getLastUpdated()
                                 .toInstant()
                                 .atOffset(ZoneOffset.UTC)
                );
            }
        }

        // URL + Search Mode
        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);

        // Raw JSON
        entity.setResourceJson(
                context.newJsonParser()
                        .encodeResourceToString(procedure)
        );

        return entity;
    }

    // Extract ID
    private static String extractId(String reference) {

        if (reference == null) return null;

        return reference.contains("/")
                ? reference.substring(reference.lastIndexOf("/") + 1)
                : reference;
    }
}
