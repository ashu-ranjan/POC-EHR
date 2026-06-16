package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.ConditionEntity;

public interface ConditionRepository extends JpaRepository<ConditionEntity, String>{

}
