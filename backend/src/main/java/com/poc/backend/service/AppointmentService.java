package com.poc.backend.service;

import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.AppointmentEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.AppointmentMapper;
import com.poc.backend.repository.AppointmentRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private final IGenericClient fhirClient;

    public AppointmentService(AppointmentRepository repository,
                              IGenericClient fhirClient) {
        this.repository = repository;
        this.fhirClient = fhirClient;
    }

    // CREATE APPOINTMENT

    // Create Appointment to FHIR
    public Appointment create(Appointment appt) {

        // validation
        if (appt.getParticipant().isEmpty()) {
            throw new BadRequestException("At least one participant is required.");
        }

        // identifier generation
        if (appt.getIdentifier().isEmpty()) {
            Identifier id = new Identifier();
            id.setValue(IdGenerator.generateIdentifier("APT-", 5, 5));
            appt.addIdentifier(id);
        }

        // core creation
        try {
            return (Appointment) fhirClient
                    .create()
                    .resource(appt)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create Appointment.");
        }
    }

    // Save Appointment to DB
    public AppointmentEntity save(Appointment appt) {

        try {
            String id = appt.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/Appointment/")
                    .toUriString() + id;

            return repository.save(
                    AppointmentMapper.toEntity(appt, fullUrl, "match"));

        } catch (Exception e) {
            throw new DatabaseException("Failed to save Appointment.");
        }
    }

    // UPDATE APPOINTMENT
    
    public Appointment update(String id, Appointment appt) {

        // validation
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            Appointment existing =
                    (Appointment) fhirClient
                            .read()
                            .resource(Appointment.class)
                            .withId(id)
                            .execute();

            appt.setIdentifier(existing.getIdentifier());

            appt.setId(id);

            return (Appointment) fhirClient
                    .update()
                    .resource(appt)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update Appointment.");
        }
    }
}
