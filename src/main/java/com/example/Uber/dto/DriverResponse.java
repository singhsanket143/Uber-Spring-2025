package com.example.Uber.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String licenseNumber;
    private String vehicleModel;
    private String vehiclePlateNumber;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

