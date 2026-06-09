package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.PractitionerEntity;

public interface PractitionerRepository extends JpaRepository<PractitionerEntity, String>{

}
