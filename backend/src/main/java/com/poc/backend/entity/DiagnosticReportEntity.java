package com.poc.backend.entity;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "diagnostic_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticReportEntity {

    // Id 
    @Id
    private String id;

    // Status
    private String status;

    // Report Name
    @Column(name = "code_text")
    private String codeText;

    // Category
    private String category;

    // Identifier
    @Column(columnDefinition = "TEXT")
    private String identifier;

    // Dates
    private OffsetDateTime effectiveDate;
    private OffsetDateTime issuedDate;

    // Conclusion
    @Column(columnDefinition = "TEXT")
    private String conclusion;

    // Specimen
    private String specimen;

    // Presented Form
    private String presentedForm;

    // Performer
    private String performer;

    // Notes
    @Column(columnDefinition = "TEXT")
    private String note;

    // Relations
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name = "encounter_id")
    private EncounterEntity encounter;

    @ManyToMany
    @JoinTable(name = "diagnostic_report_observation",
        joinColumns = @JoinColumn(name = "diagnoistic_report_id"),
        inverseJoinColumns = @JoinColumn(name = "observation_id")
    )
    private List<ObservationEntity> observations;

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
