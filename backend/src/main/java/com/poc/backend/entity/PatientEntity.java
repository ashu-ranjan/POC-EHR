package com.poc.backend.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient")
public class PatientEntity {

    // Id
    @Id
    private String id;

    // Name
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // Gender
    @Column(name = "gender")
    private String gender;

    // Identifier
    @Column(columnDefinition = "TEXT")
    private String identifier;

    // DOB
    @Column(name = "birth_date")
    private LocalDate birthDate;

    // Meta
    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated;

    @Column(name = "version_id")
    private String versionId;

    private String source;

    @Column(name = "full_url")
    private String fullUrl;

    @Column(name = "search_mode")
    private String searchMode;

    // Resource Json
    @Lob
    @Column(name = "resource_json", columnDefinition = "TEXT")
    private String resourceJson;


    


}
