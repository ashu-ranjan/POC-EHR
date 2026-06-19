package com.poc.backend.mapper;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.DiagnosticReport;

import com.poc.backend.entity.DiagnosticReportEntity;
import com.poc.backend.entity.EncounterEntity;
import com.poc.backend.entity.ObservationEntity;
import com.poc.backend.entity.PatientEntity;

import ca.uhn.fhir.context.FhirContext;

public class DiagnosticReportMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static DiagnosticReportEntity toEntity(DiagnosticReport diagnosticReport, String fullUrl, String searchMode){
        DiagnosticReportEntity entity = new DiagnosticReportEntity();

        // Id
        entity.setId(diagnosticReport.getIdElement().getIdPart());

        // Status
        if(diagnosticReport.hasStatus()){
            entity.setStatus(diagnosticReport.getStatus().toCode());

        }

        // Identifier
        if(!diagnosticReport.getIdentifier().isEmpty()){
            entity.setIdentifier(diagnosticReport.getIdentifierFirstRep().getValue());
        }

        // Code
        if(diagnosticReport.hasCode()){
            if(diagnosticReport.getCode().hasText()){
                entity.setCodeText(diagnosticReport.getCode().getText());
            } else if(!diagnosticReport.getCode().getCoding().isEmpty()){
                entity.setCodeText(diagnosticReport.getCode().getCodingFirstRep().getDisplay());
            }
        }


        // Category
        
        if (!diagnosticReport.getCategory().isEmpty()) {
            if (diagnosticReport.getCategoryFirstRep().hasText()) {
                entity.setCategory(diagnosticReport.getCategoryFirstRep().getText());
            } else if (!diagnosticReport.getCategoryFirstRep().getCoding().isEmpty()) {
                entity.setCategory(diagnosticReport.getCategoryFirstRep().getCodingFirstRep().getCode()
                );
            }
        }

        // Dates
        
        if (diagnosticReport.hasEffectiveDateTimeType()) {
            entity.setEffectiveDate(
                    diagnosticReport.getEffectiveDateTimeType()
                        .getValue()
                        .toInstant()
                        .atOffset(ZoneOffset.UTC)
            );
        }

        if (diagnosticReport.getIssued() != null) {
            entity.setIssuedDate(
                    diagnosticReport
                        .getIssued()
                        .toInstant()
                        .atOffset(ZoneOffset.UTC)
            );
        }


        // Conclusion
        if(diagnosticReport.hasConclusion()){
            entity.setConclusion(diagnosticReport.getConclusion());
        }

        // Specimen
        if(!diagnosticReport.getSpecimen().isEmpty() && diagnosticReport.getSpecimenFirstRep().hasDisplay()){
            entity.setSpecimen(diagnosticReport.getSpecimenFirstRep().getDisplay());
        }

        // Presented Form
        if(!diagnosticReport.getPresentedForm().isEmpty()){
            entity.setPresentedForm(diagnosticReport.getPresentedFormFirstRep().getTitle());
        }

        // Performer
        if(diagnosticReport.getPerformer().isEmpty()){
            entity.setPerformer(diagnosticReport.getPerformerFirstRep().getDisplay());
        }

        // Notes
        if(diagnosticReport.hasConclusion()){
            entity.setNote(diagnosticReport.getConclusion());
        }

        // Subject
        if(diagnosticReport.hasSubject() && diagnosticReport.getSubject().hasReference()){
            String id = extractId(diagnosticReport.getSubject().getReference());

            PatientEntity patient = new PatientEntity();
            patient.setId(id);
            entity.setPatient(patient);
        }

        // Encounter
        if(diagnosticReport.hasEncounter() && diagnosticReport.getEncounter().hasReference()){
            String id = extractId(diagnosticReport.getEncounter().getReference());

            EncounterEntity encounter = new EncounterEntity();
            encounter.setId(id);

            entity.setEncounter(encounter);
        }

        // Observation
        if(diagnosticReport.getResult().isEmpty()){
            List<ObservationEntity> observations = diagnosticReport.getResult()
                                                            .stream()
                                                            .map(ref -> {String obsId = extractId(ref.getReference());
                                                                ObservationEntity obs = new ObservationEntity();
                                                                obs.setId(obsId);
                                                                return obs;
                                                            })
                                                            .collect(Collectors.toList());

            entity.setObservations(observations);                                               
        }

        // Meta
        if(diagnosticReport.getMedia() != null){
            entity.setVersionId(diagnosticReport.getMeta().getVersionId());
            if(diagnosticReport.getMeta().getLastUpdated() != null){
                entity.setLastUpdated(diagnosticReport.getMeta()
                                              .getLastUpdated()
                                              .toInstant()
                                              .atOffset(ZoneOffset.UTC)
            );
            }
        }

        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);

        // Resource Json
        entity.setResourceJson(context.newJsonParser().encodeResourceToString(diagnosticReport));

        return entity;
    }

    // Extract ID

    private static String extractId(String reference){
        if(reference == null) return null;
        return reference.contains("/")
                        ? reference.substring(reference.lastIndexOf("/") + 1)
                        : reference;
    }

}
