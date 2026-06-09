package com.poc.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.PractitionerRoleEntity;

public interface PractitionerRoleRepository extends JpaRepository<PractitionerRoleEntity, String> {

    List<PractitionerRoleEntity> findByPractitionerId(String practitionerId);

}
