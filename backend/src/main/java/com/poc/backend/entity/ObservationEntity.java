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
@Table(name = "observation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObservationEntity {

    // Id
    @Id
    private String id;

    // Status (final / preliminary)
    private String status;

    // Code
    @Column(name = "code_text")
    private String codeText;

    // category (Lab/vital)
    private String category;

    // Value
    private Double value;

    private String unit;

    // Identifier
    @Column(columnDefinition = "TEXT")
    private String identifier;

    // Reference range
    private Double refLow;
    private Double refHigh;

    // Interpretation (high/ low)
    private String interpretation;

    // Effective date 
    private OffsetDateTime effectiveDate;

    // Notes // 
    @Column(columnDefinition = "TEXT")
    private String note;

    // Patient relation
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    // Encounter Relation
    @ManyToOne
    @JoinColumn(name = "encounter_id")
    private EncounterEntity encounter;

    // Practitioner Relation
    @ManyToOne
    @JoinColumn(name = "practitioner_id")
    private PractitionerEntity practitioner;

    // Meta
    @Column(name = "version_id")
    private String versionId;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated;

    private String fullUrl;
    private String searchMode;

    // Resource Json
    @Lob
    @Column(name = "resource_json",columnDefinition = "TEXT")
    private String resourceJson;


}
