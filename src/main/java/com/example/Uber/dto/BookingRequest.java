package com.example.Uber.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class BookingRequest {
    
    @NotNull(message = "Passenger ID is required")
    private Long passengerId;
    
    private Long driverId;
    
    @NotBlank(message = "Pickup location lat is required")
    private Double pickupLocationLatitude;

    @NotBlank(message = "Pickup location long is required")
    private Double pickupLocationLongitude;
    
    private String dropoffLocation;
    
    private BigDecimal fare;
    
    private LocalDateTime scheduledPickupTime;
}

