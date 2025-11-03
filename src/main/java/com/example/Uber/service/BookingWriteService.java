package com.example.Uber.service;

import com.example.Uber.dto.BookingRequest;
import com.example.Uber.dto.BookingResponse;
import com.example.Uber.entity.Booking;

/**
 * Interface for Booking write operations
 * Following Interface Segregation Principle
 */
public interface BookingWriteService {
    BookingResponse create(BookingRequest request);
    BookingResponse update(Long id, BookingRequest request);
    BookingResponse updateStatus(Long id, Booking.BookingStatus status);
    void deleteById(Long id);
}

