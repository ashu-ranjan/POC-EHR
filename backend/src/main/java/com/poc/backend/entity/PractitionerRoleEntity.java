package com.poc.backend.entity;

import java.time.LocalDate;

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
@Table(name = "practitioner_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PractitionerRoleEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "practitioner_id" , nullable = false)
    private PractitionerEntity practitioner;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "role_display")
    private String roleDisplay;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Lob
    @Column(name = "resource_json", columnDefinition = "TEXT")
    private String resourceJson;


}
