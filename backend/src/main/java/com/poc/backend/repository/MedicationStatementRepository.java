package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.MedicationStatementEntity;

public interface MedicationStatementRepository extends JpaRepository<MedicationStatementEntity, String>{

}
