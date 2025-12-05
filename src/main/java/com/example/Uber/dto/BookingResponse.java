package com.example.Uber.dto;

import com.example.Uber.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private Long passengerId;
    private String passengerName;
    private Long driverId;
    private String driverName;
    private Double pickupLocationLatitude;
    private Double pickupLocationLongitude;
    private String dropoffLocation;
    private Booking.BookingStatus status;
    private BigDecimal fare;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime scheduledPickupTime;
    private LocalDateTime actualPickupTime;
    private LocalDateTime completedAt;
}

