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

    @Id
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String identifier;

    private boolean active;

    @Column(name = "version_id")
    private String versionId;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated;

    @Column(name = "full_url")
    private String fullUrl;

    @Column(name = "search_mode")
    private String searchMode;

    @Lob
    @Column(name = "resource_json", columnDefinition = "TEXT")
    private String resourceJson;
}
