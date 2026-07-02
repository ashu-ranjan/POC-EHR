package com.poc.backend.entity;

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
@Table(name = "practitioner")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PractitionerEntity {

    // Id
    @Id
    private String id;

    // Name and status
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private boolean active;

    // Identifier
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String identifier;

    // Meta
    @Column(name = "version_id")
    private String versionId;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated;

    @Column(name = "full_url")
    private String fullUrl;

    @Column(name = "search_mode")
    private String searchMode;

    // Resource Json
    @Lob
    @Column(name = "resource_json", columnDefinition = "TEXT")
    private String resourceJson;
}
