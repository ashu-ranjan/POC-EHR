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
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEntity {

    @Id
    private String id;

    // status
    private String status;

    // description
    private String description;

    // start & end time
    @Column(name = "start_time")
    private OffsetDateTime start;

    @Column(name = "end_time")
    private OffsetDateTime end;

    // duration 
    private Integer minutesDuration;

    // identifier
    private String identifier;

    // relations
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name = "practitioner_id")
    private PractitionerEntity practitioner;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    // meta
    private String versionId;
    private OffsetDateTime lastUpdated;

    private String fullUrl;
    private String searchMode;

    // Resource Json
    @Lob
    @Column(columnDefinition = "TEXT")
    private String resourceJson;
}

