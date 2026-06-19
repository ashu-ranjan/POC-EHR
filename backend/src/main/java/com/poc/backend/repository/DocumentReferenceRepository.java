package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.DocumentReferenceEntity;

public interface DocumentReferenceRepository extends JpaRepository<DocumentReferenceEntity, String>{

    
}
