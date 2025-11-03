package com.example.Uber.service;

import com.example.Uber.dto.DriverRequest;
import com.example.Uber.dto.DriverResponse;

/**
 * Interface for Driver write operations
 * Following Interface Segregation Principle
 */
public interface DriverWriteService {
    DriverResponse create(DriverRequest request);
    DriverResponse update(Long id, DriverRequest request);
    void deleteById(Long id);
}

