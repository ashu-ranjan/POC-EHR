package com.poc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "allergy_intolerance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllergyIntoleranceEntity {

    @Id
    private String id;

    private String type;

    private String clinicalStatus;
    private String verificationStatus;

    private String code;

    
    private String reaction;
    private String severity;

    @Column(columnDefinition = "TEXT")
    private String identifier;

    private OffsetDateTime recordedDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name = "practitioner_id")
    private PractitionerEntity practitioner;

    @ManyToOne
    @JoinColumn(name = "encounter_id")
    private EncounterEntity encounter;

    private String versionId;
    private OffsetDateTime lastUpdated;

    private String fullUrl;
    private String searchMode;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String resourceJson;
}
