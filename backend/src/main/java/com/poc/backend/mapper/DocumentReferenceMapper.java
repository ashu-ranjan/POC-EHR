package com.poc.backend.mapper;

import java.time.ZoneOffset;

import org.hl7.fhir.r4.model.DocumentReference;

import com.poc.backend.entity.*;

import ca.uhn.fhir.context.FhirContext;

public class DocumentReferenceMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static DocumentReferenceEntity toEntity(
            DocumentReference doc,
            String fullUrl,
            String searchMode) {

        DocumentReferenceEntity entity = new DocumentReferenceEntity();

        // ID
        entity.setId(doc.getIdElement().getIdPart());

        // Status
        if (doc.hasStatus()) {
            entity.setStatus(doc.getStatus().toCode());
        }

        // Identifier 
        if (doc.hasMasterIdentifier()) {
            entity.setIdentifier(
                    doc.getMasterIdentifier().getValue()
            );
        }

        // Type
        if (doc.hasType()) {
            if (doc.getType().hasText()) {
                entity.setType(doc.getType().getText());
            } else if (!doc.getType().getCoding().isEmpty()) {
                entity.setType(
                        doc.getType()
                           .getCodingFirstRep()
                           .getDisplay()
                );
            }
        }

        // Date
        if (doc.hasDate()) {
            entity.setDate(
                doc.getDate()
                   .toInstant()
                   .atOffset(ZoneOffset.UTC)
            );
        }

        // File (MOST IMPORTANT 🔥)
        if (!doc.getContent().isEmpty()
                && doc.getContentFirstRep().hasAttachment()) {

            var att = doc.getContentFirstRep().getAttachment();

            entity.setContentType(att.getContentType());
            entity.setFileUrl(att.getUrl());
        }

        // Patient
        if (doc.hasSubject() && doc.getSubject().hasReference()) {

            String patientId = extractId(doc.getSubject().getReference());

            PatientEntity patient = new PatientEntity();
            patient.setId(patientId);

            entity.setPatient(patient);
        }

        // Encounter (context)
        if (doc.hasContext()
                && !doc.getContext().getEncounter().isEmpty()) {

            String encId = extractId(
                    doc.getContext()
                       .getEncounterFirstRep()
                       .getReference()
            );

            EncounterEntity encounter = new EncounterEntity();
            encounter.setId(encId);

            entity.setEncounter(encounter);
        }

        // Meta
        if (doc.getMeta() != null) {

            entity.setVersionId(doc.getMeta().getVersionId());

            if (doc.getMeta().getLastUpdated() != null) {
                entity.setLastUpdated(
                        doc.getMeta()
                           .getLastUpdated()
                           .toInstant()
                           .atOffset(ZoneOffset.UTC)
                );
            }
        }

        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);

        // Raw JSON
        entity.setResourceJson(
                context.newJsonParser()
                        .encodeResourceToString(doc)
        );

        return entity;
    }

    private static String extractId(String reference) {
        if (reference == null) return null;

        return reference.contains("/")
                ? reference.substring(reference.lastIndexOf("/") + 1)
                : reference;
    }
}
