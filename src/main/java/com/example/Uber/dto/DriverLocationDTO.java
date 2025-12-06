package com.example.Uber.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverLocationDTO {
    private Integer driverId;
    private Double latitude;
    private Double longitude;
}
