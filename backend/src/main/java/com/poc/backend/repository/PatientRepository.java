package com.poc.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.PatientEntity;

public interface PatientRepository extends JpaRepository<PatientEntity, String>{

   boolean existsByEmail(String email);
   
   Optional<PatientEntity> findByEmail(String email);
}
