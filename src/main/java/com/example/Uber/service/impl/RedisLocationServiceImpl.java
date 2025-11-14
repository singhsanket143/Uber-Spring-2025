package com.example.Uber.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.example.Uber.dto.DriverLocationDTO;
import com.example.Uber.service.LocationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisLocationServiceImpl implements LocationService {

    private static final String DRIVER_GEO_OPS_KEY = "driver:geo"; // represent location of driver entity in redis

    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public Boolean saveDriverLocation(String driverId, Double latitude, Double longitude) {

        GeoOperations<String, String> geoOperations = stringRedisTemplate.opsForGeo();

        geoOperations.add(DRIVER_GEO_OPS_KEY, 
            new RedisGeoCommands.GeoLocation<>(driverId, new Point(latitude, longitude))
        );
        
        return true;
    }

    @Override
    public List<DriverLocationDTO> getNearbyDrivers(Double latitude, Double longitude, Double radius) {
        GeoOperations<String, String> geoOperations = stringRedisTemplate.opsForGeo();

        Distance circleRadius = new Distance(radius, Metrics.KILOMETERS);

        Circle circle = new Circle(new Point(latitude, longitude), circleRadius);

        GeoResults<GeoLocation<String>> results = geoOperations.radius(DRIVER_GEO_OPS_KEY, circle); // query redis

        List<DriverLocationDTO> driverLocations = new ArrayList<>();

        for(GeoResult<GeoLocation<String>> result : results) {

            Point point = geoOperations.position(DRIVER_GEO_OPS_KEY, result.getContent().getName()).get(0); // location of individual driver in redis

            DriverLocationDTO driverLocation = DriverLocationDTO.builder()
            .driverId(result.getContent().getName())
            .latitude(point.getY())
            .longitude(point.getX())
            .build();

            driverLocations.add(driverLocation);
        }
        
        return driverLocations;
    }
}


/**
 * - Booking creation flow
 * 
 *  1. User requests a booking with their lat and long coordinate
 *  2. System should create a booking with pending status
 *  3. System should search for nearby drivers within a radius of X kilometers
 *  4. Once found we will raise a request to the drivers UI to check if they want to accept the booking
 *  5. Which ever driver accepts the booking, we will allot that driver to the booking and update the status to confirmed. 
 * 
 *  Note: During this process, while the drivers are checking the booking, the user should not be blocked, they will get a response
 * of booking is pending. 
 */