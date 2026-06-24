package com.poc.backend.mapper;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;

import com.poc.backend.entity.EncounterEntity;
import com.poc.backend.entity.ObservationEntity;
import com.poc.backend.entity.PatientEntity;
import com.poc.backend.entity.PractitionerEntity;

import ca.uhn.fhir.context.FhirContext;

public class ObservationMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static ObservationEntity toEntity(Observation observation, String fullUrl, String searchMode){
        ObservationEntity entity = new ObservationEntity();

        // Id
        entity.setId(observation.getIdElement().getIdPart());

        // Status
        if(observation.hasStatus()){
            entity.setStatus(observation.getStatus().toCode());
        }

        // Identifier
        if(!observation.getIdentifier().isEmpty()){
            entity.setIdentifier(observation.getIdentifierFirstRep().getValue());
        }

        // Code
        if(observation.hasCode()){
            if(observation.getCode().hasText()){
                entity.setCodeText(observation.getCode().getText());
            } else if(!observation.getCode().getCoding().isEmpty()){
                entity.setCodeText(observation.getCode().getCodingFirstRep().getDisplay());
            }
        }

        // Category
        if(!observation.getCategory().isEmpty()){
            if(observation.getCategoryFirstRep().hasText()){
                entity.setCategory(observation.getCategoryFirstRep().getText());
            } else if(!observation.getCategoryFirstRep().getCoding().isEmpty()){
                entity.setCategory(observation.getCategoryFirstRep().getCodingFirstRep().getCode());

            }
        }

        // ValueQuantity
        if(observation.hasValueQuantity()){
            if(observation.getValueQuantity().getValue() != null){
                entity.setValue(observation.getValueQuantity().getValue().doubleValue());
            }
            entity.setUnit(observation.getValueQuantity().getUnit());
        }

        // Reference range
        if(!observation.getReferenceRange().isEmpty()){
            if(observation.getReferenceRangeFirstRep().getLow() != null){
                entity.setRefLow(observation.getReferenceRangeFirstRep().getLow().getValue().doubleValue());
            }
            if(observation.getReferenceRangeFirstRep().getHigh() != null){
                entity.setRefHigh(observation.getReferenceRangeFirstRep().getHigh().getValue().doubleValue());
            }
        }

        // Interpretation
        if(!observation.getInterpretation().isEmpty()){

            String interpretation = observation.getInterpretation()
                .stream()
                .map(i -> {
                    if(i.hasText()){
                        return i.getText();          
                    } else if(!i.getCoding().isEmpty()){
                        Coding coding = i.getCodingFirstRep();
                        return coding.hasDisplay() 
                                ? coding.getDisplay()
                                : coding.getCode();
                    }
                    return null;
                })
                .filter(v -> v != null)
                .collect(Collectors.joining(","));

            entity.setInterpretation(interpretation);
        }

        // Effective Date (when taken)
        if(observation.hasEffectiveDateTimeType()){
            entity.setEffectiveDate(observation.getEffectiveDateTimeType()
                                            .getValue()
                                            .toInstant()
                                            .atOffset(ZoneOffset.UTC));      
        }

        // Notes
        if(!observation.getNote().isEmpty()){
            String note = observation.getNote()
                                    .stream()
                                    .map(n -> n.getText())
                                    .collect(Collectors.joining(","));
            entity.setNote(note);
        }

        // Patient 
        if(observation.hasSubject() && observation.getSubject().hasReference()){
            String id = extractId(observation.getSubject().getReference());

            PatientEntity patient = new PatientEntity();
            patient.setId(id);
            entity.setPatient(patient);
        }

        // Encounter
        if(observation.hasEncounter() && observation.getEncounter().hasReference()){
            String id = extractId(observation.getEncounter().getReference());

            EncounterEntity encounter = new EncounterEntity();
            encounter.setId(id);

            entity.setEncounter(encounter);
        }

        // Practitioner (performer)
        if(!observation.getPerformer().isEmpty() && observation.getPerformerFirstRep().hasReference()){
            String id = extractId(observation.getPerformerFirstRep().getReference());

            PractitionerEntity practitioner = new PractitionerEntity();
            practitioner.setId(id);

            entity.setPractitioner(practitioner);
        }

        // Meta 
        if(observation.getMeta() != null){
            entity.setVersionId(observation.getMeta().getVersionId());
            if(observation.getMeta().getLastUpdated() != null){
                entity.setLastUpdated(observation.getMeta().getLastUpdated().toInstant().atOffset(ZoneOffset.UTC));
            }
        }
        
        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);

        // Resource Json
        entity.setResourceJson(context.newJsonParser().encodeResourceToString(observation));

        return entity;

    }

    // Extract Id
    private static String extractId(String reference){
        if(reference == null) return null;
        return reference.contains("/")
                        ? reference.substring(reference.lastIndexOf("/") + 1)
                        : reference;
    }


}
