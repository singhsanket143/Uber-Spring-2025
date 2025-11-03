package com.example.Uber.service;

import com.example.Uber.dto.BookingResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface for Booking read operations
 * Following Interface Segregation Principle
 */
public interface BookingReadService {
    Optional<BookingResponse> findById(Long id);
    List<BookingResponse> findAll();
    List<BookingResponse> findByPassengerId(Long passengerId);
    List<BookingResponse> findByDriverId(Long driverId);
}

