package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.AllergyIntoleranceEntity;

public interface AllergyIntoleranceRepository extends JpaRepository<AllergyIntoleranceEntity, String>{

}
