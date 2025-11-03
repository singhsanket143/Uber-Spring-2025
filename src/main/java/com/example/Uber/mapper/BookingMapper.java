package com.example.Uber.mapper;

import com.example.Uber.dto.BookingRequest;
import com.example.Uber.dto.BookingResponse;
import com.example.Uber.entity.Booking;
import com.example.Uber.entity.Driver;
import com.example.Uber.entity.Passenger;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    
    public Booking toEntity(BookingRequest request, Passenger passenger, Driver driver) {
        Booking.BookingStatus status = driver != null ? Booking.BookingStatus.CONFIRMED : Booking.BookingStatus.PENDING;
        
        return Booking.builder()
                .passenger(passenger)
                .driver(driver)
                .pickupLocation(request.getPickupLocation())
                .dropoffLocation(request.getDropoffLocation())
                .fare(request.getFare())
                .status(status)
                .scheduledPickupTime(request.getScheduledPickupTime())
                .build();
    }
    
    public BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .passengerId(booking.getPassenger() != null ? booking.getPassenger().getId() : null)
                .passengerName(booking.getPassenger() != null ? booking.getPassenger().getName() : null)
                .driverId(booking.getDriver() != null ? booking.getDriver().getId() : null)
                .driverName(booking.getDriver() != null ? booking.getDriver().getName() : null)
                .pickupLocation(booking.getPickupLocation())
                .dropoffLocation(booking.getDropoffLocation())
                .status(booking.getStatus())
                .fare(booking.getFare())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .scheduledPickupTime(booking.getScheduledPickupTime())
                .actualPickupTime(booking.getActualPickupTime())
                .completedAt(booking.getCompletedAt())
                .build();
    }
    
    public void updateEntity(Booking booking, BookingRequest request, Passenger passenger, Driver driver) {
        booking.setPassenger(passenger);
        booking.setDriver(driver);
        booking.setPickupLocation(request.getPickupLocation());
        booking.setDropoffLocation(request.getDropoffLocation());
        booking.setFare(request.getFare());
        booking.setScheduledPickupTime(request.getScheduledPickupTime());
        
        // Update status based on driver assignment
        if (driver != null && booking.getStatus() == Booking.BookingStatus.PENDING) {
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
        }
    }
}

