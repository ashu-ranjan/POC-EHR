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
@Table(name = "organization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationEntity {

    // Id
    @Id
    private String id;
    
    // Name and Status
    private String name;
    private Boolean active;

    // Identifier
    @Column(name = "identifier", columnDefinition = "TEXT")
    private String identifier;

    // Type
    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "type_display")
    private String typeDisplay;

    // Contact
    @Column(name = "telecom", columnDefinition = "TEXT")
    private String telecom;

    // Address
    private String address;

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
