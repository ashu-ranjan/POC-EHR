package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.EncounterEntity;

public interface EncounterRepository extends JpaRepository<EncounterEntity, String>{

}
