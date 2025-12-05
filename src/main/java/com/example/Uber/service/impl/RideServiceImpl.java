package com.example.Uber.service.impl;

import org.springframework.stereotype.Service;

import com.example.Uber.RideAcceptanceRequest;
import com.example.Uber.RideAcceptanceResponse;
import com.example.Uber.RideServiceGrpc;
import com.example.Uber.service.BookingService;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RideServiceImpl extends RideServiceGrpc.RideServiceImplBase {

    private final BookingService bookingService;

    @Override
    public void acceptRide(RideAcceptanceRequest request, StreamObserver<RideAcceptanceResponse> responseObserver) {
        // Call the BookingService to update the ride with the new driver id.
    }
    
}
