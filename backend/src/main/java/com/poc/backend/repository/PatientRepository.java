package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.PatientEntity;

public interface PatientRepository extends JpaRepository<PatientEntity, String>{
   
}
