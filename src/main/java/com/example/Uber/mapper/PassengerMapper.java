package com.example.Uber.mapper;

import com.example.Uber.dto.PassengerRequest;
import com.example.Uber.dto.PassengerResponse;
import com.example.Uber.entity.Passenger;
import org.springframework.stereotype.Component;

@Component
public class PassengerMapper {
    
    public Passenger toEntity(PassengerRequest request) {
        return Passenger.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }
    
    public PassengerResponse toResponse(Passenger passenger) {
        return PassengerResponse.builder()
                .id(passenger.getId())
                .name(passenger.getName())
                .email(passenger.getEmail())
                .phoneNumber(passenger.getPhoneNumber())
                .createdAt(passenger.getCreatedAt())
                .updatedAt(passenger.getUpdatedAt())
                .build();
    }
    
    public void updateEntity(Passenger passenger, PassengerRequest request) {
        passenger.setName(request.getName());
        passenger.setEmail(request.getEmail());
        passenger.setPhoneNumber(request.getPhoneNumber());
    }
}

