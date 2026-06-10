package com.poc.backend.entity;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "practitioner_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PractitionerRoleEntity {

    // Id and status
    @Id
    private String id;
    private boolean active;

    // Many To One Relation with the PractitionerEntity
    @ManyToOne
    @JoinColumn(name = "practitioner_id" , nullable = false)
    private PractitionerEntity practitioner;

    // Many To One Relation with OrganizationEntity
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private OrganizationEntity organization;

    // Many To Many Realtion with LocationEntity
    @ManyToMany
    @JoinTable(
        name = "practitioner_role_location",
        joinColumns = @JoinColumn(name = "practitioner_role_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private List<LocationEntity> location;

    // Code and Display
    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "role_display")
    private String roleDisplay;

    // Speciality
    @Column(name = "specialty")
    private String specialty;

    // Telecom
    @Column(columnDefinition = "TEXT")
    private String telecom;

    // Meta
    private OffsetDateTime periodStart;
    private OffsetDateTime periodEnd;

    private String fullUrl;
    private String searchMode;

    private String versionId;
    private OffsetDateTime lastUpdated;

    // Resource Json
    @Lob
    @Column(name = "resource_json", columnDefinition = "TEXT")
    private String resourceJson;


}
