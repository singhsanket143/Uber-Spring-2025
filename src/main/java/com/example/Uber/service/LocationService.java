package com.example.Uber.service;

import java.util.List;

import com.example.Uber.dto.DriverLocationDTO;

public interface LocationService {

    Boolean saveDriverLocation(Integer driverId, Double latitude, Double longitude);

    List<DriverLocationDTO> getNearbyDrivers(Double latitude, Double longitude, Double radius);
    
}
