package com.example.Uber.service.impl;

import com.example.Uber.client.GrpcClient;
import com.example.Uber.dto.BookingRequest;
import com.example.Uber.dto.BookingResponse;
import com.example.Uber.dto.DriverLocationDTO;
import com.example.Uber.entity.Booking;
import com.example.Uber.entity.Driver;
import com.example.Uber.entity.Passenger;
import com.example.Uber.mapper.BookingMapper;
import com.example.Uber.repository.BookingRepository;
import com.example.Uber.repository.DriverRepository;
import com.example.Uber.repository.PassengerRepository;
import com.example.Uber.service.BookingService;
import com.example.Uber.service.LocationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;
    private final LocationService locationService;
    private final BookingMapper bookingMapper;
    private final GrpcClient grpcClient;
    
    @Override
    @Transactional(readOnly = true)
    public Optional<BookingResponse> findById(Long id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> findAll() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> findByPassengerId(Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found with id: " + passengerId));
        return bookingRepository.findByPassenger(passenger).stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> findByDriverId(Long driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + driverId));
        return bookingRepository.findByDriver(driver).stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public BookingResponse create(BookingRequest request) {
        // Validate and fetch passenger
        Passenger passenger = passengerRepository.findById(request.getPassengerId())
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found with id: " + request.getPassengerId()));
        
        // Handle driver assignment if provided
        Driver driver = null;
        Booking.BookingStatus status = Booking.BookingStatus.PENDING;
        
        if (request.getDriverId() != null) {
            driver = driverRepository.findById(request.getDriverId())
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + request.getDriverId()));
            
            // Check if driver is available
            if (!driver.getIsAvailable()) {
                throw new IllegalStateException("Driver with id " + request.getDriverId() + " is not available");
            }
            
            // Assign driver and mark as unavailable
            driver.setIsAvailable(false);
            driverRepository.save(driver);
            status = Booking.BookingStatus.CONFIRMED;
        }
        
        // // Convert latitude/longitude from Double to String
        String pickupLat = request.getPickupLocationLatitude() != null 
                ? request.getPickupLocationLatitude().toString() 
                : null;
        String pickupLng = request.getPickupLocationLongitude() != null 
                ? request.getPickupLocationLongitude().toString() 
                : null;
        
        if (pickupLat == null || pickupLng == null) {
            throw new IllegalArgumentException("Pickup location latitude and longitude are required");
        }
        
        // // Set default fare if not provided
        BigDecimal fare = request.getFare();
        if (fare == null) {
            fare = BigDecimal.ZERO; // Default fare, can be calculated later
        }
        
        // // Create booking
        Booking newBooking = Booking.builder()
                .passenger(passenger)
                .driver(driver)
                .pickupLocationLatitude(pickupLat)
                .pickupLocationLongitude(pickupLng)
                .dropoffLocation(request.getDropoffLocation())
                .status(status)
                .fare(fare)
                .scheduledPickupTime(request.getScheduledPickupTime())
                .build();
        
        Booking savedBooking = bookingRepository.save(newBooking);
        
        // Find the nearby drivers and then trigger an RPC to UberSocketService to notify them

        List<DriverLocationDTO> nearbyDrivers = locationService.getNearbyDrivers(Double.parseDouble(pickupLat), Double.parseDouble(pickupLng), 10.0);
        
        grpcClient.notifyDriversForNewRide(pickupLat, pickupLng, Integer.parseInt(savedBooking.getId().toString()), nearbyDrivers.stream().map(DriverLocationDTO::getDriverId).collect(Collectors.toList()));
        return bookingMapper.toResponse(savedBooking);
    }
    
    @Override
    public BookingResponse update(Long id, BookingRequest request) {
        // Booking booking = bookingRepository.findById(id)
        //         .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        
        // // Validate and fetch passenger if changed
        // Passenger passenger = booking.getPassenger();
        // if (request.getPassengerId() != null && !request.getPassengerId().equals(booking.getPassenger().getId())) {
        //     passenger = passengerRepository.findById(request.getPassengerId())
        //             .orElseThrow(() -> new IllegalArgumentException("Passenger not found with id: " + request.getPassengerId()));
        // }
        
        // // Handle driver assignment/change
        // Driver driver = booking.getDriver();
        // if (request.getDriverId() != null) {
        //     if (driver == null || !request.getDriverId().equals(driver.getId())) {
        //         // Release old driver if exists
        //         if (driver != null) {
        //             driver.setIsAvailable(true);
        //             driverRepository.save(driver);
        //         }
                
        //         // Assign new driver
        //         Driver newDriver = driverRepository.findById(request.getDriverId())
        //                 .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + request.getDriverId()));
                
        //         if (!newDriver.getIsAvailable()) {
        //             throw new IllegalStateException("Driver with id " + request.getDriverId() + " is not available");
        //         }
                
        //         newDriver.setIsAvailable(false);
        //         driverRepository.save(newDriver);
        //         driver = newDriver;
                
        //         // Update status if booking was pending
        //         if (booking.getStatus() == Booking.BookingStatus.PENDING) {
        //             booking.setStatus(Booking.BookingStatus.CONFIRMED);
        //         }
        //     }
        // }
        
        // // Update location fields
        // if (request.getPickupLocationLatitude() != null) {
        //     booking.setPickupLocationLatitude(request.getPickupLocationLatitude().toString());
        // }
        // if (request.getPickupLocationLongitude() != null) {
        //     booking.setPickupLocationLongitude(request.getPickupLocationLongitude().toString());
        // }
        
        // // Update other fields
        // if (request.getDropoffLocation() != null) {
        //     booking.setDropoffLocation(request.getDropoffLocation());
        // }
        // if (request.getFare() != null) {
        //     booking.setFare(request.getFare());
        // }
        // if (request.getScheduledPickupTime() != null) {
        //     booking.setScheduledPickupTime(request.getScheduledPickupTime());
        // }
        
        // booking.setPassenger(passenger);
        // booking.setDriver(driver);
        
        // Booking updatedBooking = bookingRepository.save(booking);
        // return bookingMapper.toResponse(updatedBooking);
        return null;
    }
    
    @Override
    public BookingResponse updateStatus(Long id, Booking.BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        
        booking.setStatus(status);
        
        // Handle status-specific logic
        if (status == Booking.BookingStatus.IN_PROGRESS && booking.getActualPickupTime() == null) {
            booking.setActualPickupTime(LocalDateTime.now());
        } else if (status == Booking.BookingStatus.COMPLETED) {
            booking.setCompletedAt(LocalDateTime.now());
            // Release driver
            if (booking.getDriver() != null) {
                Driver driver = booking.getDriver();
                driver.setIsAvailable(true);
                driverRepository.save(driver);
            }
        } else if (status == Booking.BookingStatus.CANCELLED) {
            // Release driver
            if (booking.getDriver() != null) {
                Driver driver = booking.getDriver();
                driver.setIsAvailable(true);
                driverRepository.save(driver);
            }
        }
        
        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toResponse(updatedBooking);
    }
    
    @Override
    public void deleteById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        
        // Release driver if assigned
        if (booking.getDriver() != null) {
            Driver driver = booking.getDriver();
            driver.setIsAvailable(true);
            driverRepository.save(driver);
        }
        
        bookingRepository.deleteById(id);
    }
}

