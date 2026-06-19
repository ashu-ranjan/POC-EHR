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
@Table(name = "procedure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureEntity {

    @Id
    private String id;

    // status
    private String status;

    // procedure name
    @Column(name = "procedure_text")
    private String procedureText;

    // category
    private String category;

    // performed date
    private OffsetDateTime performedDate;

    // body site
    private String bodySite;

    // identifier
    @Column(columnDefinition = "TEXT")
    private String identifier;

    // relations
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name = "encounter_id")
    private EncounterEntity encounter;

    // meta
    private String versionId;
    private OffsetDateTime lastUpdated;

    private String fullUrl;
    private String searchMode;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String resourceJson;
}

