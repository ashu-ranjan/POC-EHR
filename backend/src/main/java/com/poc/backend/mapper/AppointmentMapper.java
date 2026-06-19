package com.poc.backend.mapper;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.Appointment;

import com.poc.backend.entity.*;

import ca.uhn.fhir.context.FhirContext;

public class AppointmentMapper {

    private static final FhirContext context = FhirContext.forR4();

    public static AppointmentEntity toEntity(
            Appointment appt,
            String fullUrl,
            String searchMode) {

        AppointmentEntity entity = new AppointmentEntity();

        // id
        entity.setId(appt.getIdElement().getIdPart());

        // status
        if (appt.hasStatus()) {
            entity.setStatus(appt.getStatus().toCode());
        }

        // identifier
        if (!appt.getIdentifier().isEmpty()) {
            String identifier = appt.getIdentifier().stream()
                    .map(i -> i.getValue())
                    .collect(Collectors.joining(" | "));
            entity.setIdentifier(identifier);
        }

        // description
        if (appt.hasDescription()) {
            entity.setDescription(appt.getDescription());
        }

        // timing
        if (appt.hasStart()) {
            entity.setStart(
                    appt.getStart().toInstant().atOffset(ZoneOffset.UTC)
            );
        }

        if (appt.hasEnd()) {
            entity.setEnd(
                    appt.getEnd().toInstant().atOffset(ZoneOffset.UTC)
            );
        }

        if (appt.hasMinutesDuration()) {
            entity.setMinutesDuration(appt.getMinutesDuration());
        }

        // participants
        if (!appt.getParticipant().isEmpty()) {

            for (var p : appt.getParticipant()) {

                if (p.hasActor() && p.getActor().hasReference()) {

                    String ref = p.getActor().getReference();

                    if (ref.startsWith("Patient")) {
                        PatientEntity patient = new PatientEntity();
                        patient.setId(extractId(ref));
                        entity.setPatient(patient);
                    }

                    else if (ref.startsWith("Practitioner")) {
                        PractitionerEntity practitioner = new PractitionerEntity();
                        practitioner.setId(extractId(ref));
                        entity.setPractitioner(practitioner);
                    }

                    else if (ref.startsWith("Location")) {
                        LocationEntity location = new LocationEntity();
                        location.setId(extractId(ref));
                        entity.setLocation(location);
                    }
                }
            }
        }

        // meta
        if (appt.getMeta() != null) {
            entity.setVersionId(appt.getMeta().getVersionId());

            if (appt.getMeta().getLastUpdated() != null) {
                entity.setLastUpdated(
                        appt.getMeta().getLastUpdated()
                                .toInstant()
                                .atOffset(ZoneOffset.UTC)
                );
            }
        }

        entity.setFullUrl(fullUrl);
        entity.setSearchMode(searchMode);

        // raw json
        entity.setResourceJson(
                context.newJsonParser().encodeResourceToString(appt)
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