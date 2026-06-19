package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.AppointmentEntity;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, String>{

    
}
