package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.ObservationEntity;

public interface ObservationRepository extends JpaRepository<ObservationEntity, String>{

    
}
