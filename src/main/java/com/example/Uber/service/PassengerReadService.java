package com.example.Uber.service;

import com.example.Uber.dto.PassengerResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface for Passenger read operations
 * Following Interface Segregation Principle
 */
public interface PassengerReadService {
    Optional<PassengerResponse> findById(Long id);
    List<PassengerResponse> findAll();
    Optional<PassengerResponse> findByEmail(String email);
}

