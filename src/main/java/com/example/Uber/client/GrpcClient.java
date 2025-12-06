package com.example.Uber.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.Uber.RideNotificationRequest;
import com.example.Uber.RideNotificationResponse;
import com.example.Uber.RideNotificationServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;

@Component
public class GrpcClient {
    
    @Value("${grpc.server.port:9090}")
    private int grpcServerPort;

    @Value("${grpc.server.host:localhost}")
    private String grpcServerHost;

    private ManagedChannel channel;
    private RideNotificationServiceGrpc.RideNotificationServiceBlockingStub rideNotificationServiceStub;


    @PostConstruct
    public void init() {
        channel = ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort)
        .usePlaintext()
        .build();

        rideNotificationServiceStub = RideNotificationServiceGrpc.newBlockingStub(channel);
    }

    public boolean notifyDriversForNewRide(String pickUpLocationLatitude, String pickUpLocationLongitude, Integer bookingId, List<Integer> driverIds) {
        RideNotificationRequest request = RideNotificationRequest.newBuilder()
        .setPickUpLocationLatitude(pickUpLocationLatitude)
        .setPickUpLocationLongitude(pickUpLocationLongitude)
        .setBookingId(bookingId)
        .addAllDriverIds(driverIds)
        .build();

        RideNotificationResponse response = rideNotificationServiceStub.notifyDriversForNewRide(request); // make the rpc calls

        return response.getSuccess();
    }
    
    
}
