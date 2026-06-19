package com.poc.backend.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointment")
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
    private OffsetDateTime start;
    private OffsetDateTime end;

    // duration (optional)
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

    @Lob
    @Column(columnDefinition = "TEXT")
    private String resourceJson;
}
