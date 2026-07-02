package com.poc.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.PractitionerEntity;


public interface PractitionerRepository extends JpaRepository<PractitionerEntity, String>{

    boolean existsByIdentifier(String indentifier);

    boolean existsByEmail(String email);

    Optional<PractitionerEntity> findByIdentifier(String identifier);
}
