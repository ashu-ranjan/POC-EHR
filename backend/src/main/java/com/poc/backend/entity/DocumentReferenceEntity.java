package com.poc.backend.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "document_reference")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentReferenceEntity {

    // Id
    @Id
    private String id;

    // status
    private String status;

    // document type
    private String type;

    // file metadata
    private String contentType;

    @Column(name = "file_url")
    private String fileUrl;

    // document date
    private OffsetDateTime date;

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

