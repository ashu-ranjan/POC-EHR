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

    @Id
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated;

    private String source;

    @Lob
    @Column(name = "resource_json", columnDefinition = "TEXT")
    private String resourceJson;

    @Column(name = "full_url")
    private String fullUrl;

    @Column(name = "search_mode")
    private String searchMode;


}
