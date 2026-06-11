package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, String>{

}
