package com.poc.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.backend.entity.DiagnosticReportEntity;
import com.poc.backend.entity.ObservationEntity;
import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;
import com.poc.backend.mapper.DiagnosticReportMapper;
import com.poc.backend.repository.DiagnosticReportRepository;
import com.poc.backend.repository.ObservationRepository;
import com.poc.backend.utility.IdGenerator;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@Service
public class DiagnosticReportService {

    private final DiagnosticReportRepository repository;
    private final ObservationRepository observationRepository;
    private final IGenericClient fhirClient;

    public DiagnosticReportService(DiagnosticReportRepository repository,
                                   ObservationRepository observationRepository,
                                   IGenericClient fhirClient) {
        this.repository = repository;
        this.observationRepository = observationRepository;
        this.fhirClient = fhirClient;
    }

    // CREATE DIAGNOSTIC REPORT

    // Create DiagnosticReport to FHIR
    public DiagnosticReport create(DiagnosticReport diagnosticReport) {

        // validation
        if (!diagnosticReport.hasSubject()) {
            throw new BadRequestException("Patient reference is required.");
        }

        // identifier generation
        if (diagnosticReport.getIdentifier().isEmpty()) {
            Identifier id = new Identifier();
            id.setValue(IdGenerator.generateIdentifier("DRE-", 5, 5));
            diagnosticReport.addIdentifier(id);
        }

        // core creation
        try {
            return (DiagnosticReport) fhirClient
                    .create()
                    .resource(diagnosticReport)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to create DiagnosticReport.");
        }
    }

    // Save DiagnosticReport to DB
    public DiagnosticReportEntity save(DiagnosticReport diagnosticReport) {

        try {
            String id = diagnosticReport.getIdElement().getIdPart();

            String fullUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/DiagnosticReport/")
                    .toUriString() + id;

            DiagnosticReportEntity entity =
                    DiagnosticReportMapper.toEntity(diagnosticReport, fullUrl, "match");

            // observation linking (kept exactly)
            if (entity.getObservations() != null) {
                List<ObservationEntity> observations =
                        entity.getObservations()
                                .stream()
                                .map(obs -> observationRepository
                                        .findById(obs.getId())
                                        .orElse(null))
                                .filter(o -> o != null)
                                .collect(Collectors.toList());

                entity.setObservations(observations);
            }
            if (entity.getIdentifier() == null) {
                repository.findById(id)
                        .ifPresent(existing -> entity.setIdentifier(existing.getIdentifier()));
            }

            return repository.save(entity);

        } catch (Exception e) {
            throw new DatabaseException("Failed to save DiagnosticReport.");
        }
    }

    // UPDATE DIAGNOSTIC REPORT
    
    public DiagnosticReport update(String id, DiagnosticReport diagnosticReport) {

        // validation
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ID is required for update.");
        }

        // core updation
        try {

            DiagnosticReport existing =
                    (DiagnosticReport) fhirClient
                            .read()
                            .resource(DiagnosticReport.class)
                            .withId(id)
                            .execute();

            // ALWAYS preserve identifier
            diagnosticReport.setIdentifier(existing.getIdentifier());

            diagnosticReport.setId(id);

            return (DiagnosticReport) fhirClient
                    .update()
                    .resource(diagnosticReport)
                    .execute()
                    .getResource();

        } catch (Exception e) {
            throw new FHIRClientException("Failed to update DiagnosticReport.");
        }
    }
}