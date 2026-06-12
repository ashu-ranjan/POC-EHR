package com.poc.backend.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "encounter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EncounterEntity {

    // Id
    @Id
    private String id;

    // Identifier
    @Column(columnDefinition = "TEXT")
    private String identifier;

    // Statusn(planned , In-progress, finished)
    private String status;

    // Many to one relationship with Patient (Subject)
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    // Many to One relationship with Practitioner
    @ManyToOne
    @JoinColumn(name = "practitioner_id")
    private PractitionerEntity practitioner;

    // Many to One relationship with Organization
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private OrganizationEntity organization;

    // Many to One relationship with Location
    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    // Period
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;

    // Description
    private String description;

    // Meta
    @Column(name = "version_id")
    private String versionId;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated;

    private String fullUrl;
    private String searchMode;

    // Resource Json
    @Lob
    @Column(name = "resource_json", columnDefinition = "TEXT")
    private String resourceJson;

}
