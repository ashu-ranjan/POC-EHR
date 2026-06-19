package com.poc.backend.mapper;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.MedicationStatement;

import com.poc.backend.entity.MedicationStatementEntity;
import com.poc.backend.entity.PatientEntity;

import ca.uhn.fhir.context.FhirContext;
import lombok.var;

public class MedicationStatementMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static MedicationStatementEntity toEntity(MedicationStatement ms, String fullUrl, String searchMode){
        MedicationStatementEntity entity = new MedicationStatementEntity();

        // Id
        entity.setId(ms.getIdElement().getIdPart());

        // Status
        if(ms.hasStatus()){
            entity.setStatus(ms.getStatus().toCode());
        }

        // Identifier
        if(!ms.getIdentifier().isEmpty()){
            String identifier = ms.getIdentifier()
                                    .stream()
                                    .map(i -> i.getValue())
                                    .collect(Collectors.joining(","));
            entity.setIdentifier(identifier);
        }

        // Medication
        if(ms.hasMedicationCodeableConcept() && ms.getMedicationCodeableConcept().hasText()){
            entity.setMedicationText(ms.getMedicationCodeableConcept().getText());
        } else if(ms.hasMedicationReference() && ms.getMedicationReference().hasDisplay()){
            entity.setMedicationText(ms.getMedicationReference().getDisplay());
        }

        // Reason
        if(!ms.getReasonCode().isEmpty()){
            String reason = ms.getReasonCode()
                                .stream()
                                .map(r -> r.getText())
                                .collect(Collectors.joining(","));

            entity.setReason(reason);
        }

        // Dosage
        if(!ms.getDosage().isEmpty()){
            Dosage d = ms.getDosageFirstRep();

            // Text
            if(d.hasText()){
                entity.setDosageText(d.getText());
            }

            // As Needed
            if(d.hasAsNeededBooleanType()){
                entity.setAsNeeded(d.getAsNeededBooleanType().booleanValue());
            }

            // Timings
            if(d.hasTiming() && d.getTiming().hasRepeat()){
                var repeat = d.getTiming().getRepeat();
                if(repeat.hasFrequency()){
                    entity.setFrequency(repeat.getFrequency());
                }
                if(repeat.hasPeriod()){
                    entity.setPeriod(repeat.getPeriod().intValue());
                }
                if(repeat.hasPeriodUnit()){
                    entity.setPeriodUnit(repeat.getPeriodUnit().toCode());
                }
            }

            // Dose Quantity
            if(!d.getDoseAndRate().isEmpty() 
                && d.getDoseAndRateFirstRep().getDoseQuantity() != null 
                && d.getDoseAndRateFirstRep().getDoseQuantity().getValue() != null){

                    entity.setDoseValue(d.getDoseAndRateFirstRep().getDoseQuantity().getValue().doubleValue());
                    entity.setDoseUnit(d.getDoseAndRateFirstRep().getDoseQuantity().getUnit());
            }
        }

        // Patient (Subject)
        if(ms.hasSubject() && ms.getSubject().hasReference()){
            String id = extractId(ms.getSubject().getReference());

            PatientEntity patient = new PatientEntity();
            patient.setId(id);
            entity.setPatient(patient);
        }
        
        // Meta
        if (ms.getMeta() != null) {

            entity.setVersionId(ms.getMeta().getVersionId());

            if (ms.getMeta().getLastUpdated() != null) {
                entity.setLastUpdated(
                        ms.getMeta()
                          .getLastUpdated()
                          .toInstant()
                          .atOffset(ZoneOffset.UTC)
                );
            }
        }

        
        // URL + Search
        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);

        // Raw JSON
        entity.setResourceJson(
                context.newJsonParser()
                        .encodeResourceToString(ms)
        );

        return entity;
    }

    
    // Helper
    private static String extractId(String reference) {

        if (reference == null) return null;

        return reference.contains("/")
                ? reference.substring(reference.lastIndexOf("/") + 1)
                : reference;
    }

}
