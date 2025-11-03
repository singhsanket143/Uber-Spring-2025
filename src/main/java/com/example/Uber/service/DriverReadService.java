package com.example.Uber.service;

import com.example.Uber.dto.DriverResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface for Driver read operations
 * Following Interface Segregation Principle
 */
public interface DriverReadService {
    Optional<DriverResponse> findById(Long id);
    List<DriverResponse> findAll();
    Optional<DriverResponse> findByEmail(String email);
    List<DriverResponse> findAvailableDrivers();
}

