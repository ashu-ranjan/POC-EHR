package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.OrganizationEntity;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity,String>{

}
