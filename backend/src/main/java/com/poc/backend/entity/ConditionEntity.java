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
@Table(name = "condition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConditionEntity {

    // Id
    @Id
    private String id;

    // Clinical fields
    @Column(name = "clinical_status")
    private String clinicalStatus;

    @Column(name = "verification_status")
    private String verificationStatus;

    // Identifier
    @Column(columnDefinition = "TEXT")
    private String identifier;

    // Diagnosis
    @Column(name = "code_text")
    private String codeText;

    // Severity
    private String severity;

    // Category
    private String category;

    // Body Site
    private String bodySite;

    // Dates
    private OffsetDateTime onsetDate;
    private OffsetDateTime abatementDate;
    private OffsetDateTime recordedTime;

    // Symptoms
    private String evidence;

    // Notes
    @Column(columnDefinition = "TEXT")
    private String note;

    // Many to One relationship with Patient
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    // Many to One relationship with Practitioner
    @ManyToOne
    @JoinColumn(name = "practitioner_id")
    private PractitionerEntity practitioner;

    // Many to One relationship with Encounter
    @ManyToOne
    @JoinColumn(name = "encounter_id")
    private EncounterEntity encounter;

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
