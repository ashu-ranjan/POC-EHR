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
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntity {

    // Id
    @Id
    private String id;

    // Name and Status
    private String name;
    private String status;

    // Identifier
    @Column(columnDefinition = "TEXT")
    private String identifier;

    // Type (ICU, Clinic, OPD, etc.)
    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "type_display")
    private String typeDisplay;

    // Physical Type (Ward, Building, Room, etc.)
    @Column(name = "physical_type_code")
    private String physicalTypeCode;

    @Column(name = "physical_type_display")
    private String physicalTypeDisplay;

    // Contact
    @Column(columnDefinition = "TEXT")
    private String telecom;

    // Address
    private String address;

    // Geo Positions
    private Double latitude;
    private Double longitude;


    // Many to One relation with Organization Entity
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private OrganizationEntity organization;

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
