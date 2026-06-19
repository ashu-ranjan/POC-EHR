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
@Table(name = "medication_statement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationStatementEntity {

    // Id
    @Id
    private String id;

    // Status
    private String status;

    // Identifier
    @Column(columnDefinition = "TEXT")
    private String identifier;

    // Medication name
    @Column(name = "medication_text")
    private String medicationText;

    // Reason 
    private String reason;

    // Dosage summary
    @Column(columnDefinition = "TEXT")
    private String dosageText;

    // Quantity
    private Double doseValue;
    private String doseUnit;

    // Timings
    private Integer frequency;
    private Integer period;
    private String periodUnit;

    // As Needed
    private boolean asNeeded;

    // Patient(Subject)
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    // Meta
    private String versionId;
    private OffsetDateTime lastUpdated;

    private String fullUrl;
    private String searchMode;

    // Resource Json
    @Lob
    @Column(name = "resource_json", columnDefinition = "TEXT")
    private String resourceJson;


}
