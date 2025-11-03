package com.example.Uber.service;

import com.example.Uber.dto.PassengerRequest;
import com.example.Uber.dto.PassengerResponse;

/**
 * Interface for Passenger write operations
 * Following Interface Segregation Principle
 */
public interface PassengerWriteService {
    PassengerResponse create(PassengerRequest request);
    PassengerResponse update(Long id, PassengerRequest request);
    void deleteById(Long id);
}

