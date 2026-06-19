package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.ProcedureEntity;

public interface ProcedureRepository extends JpaRepository<ProcedureEntity, String>{

    
}
