package com.poc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.backend.entity.DiagnosticReportEntity;

public interface DiagnosticReportRepository extends JpaRepository<DiagnosticReportEntity, String>{

}
