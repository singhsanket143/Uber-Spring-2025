package com.example.Uber.controller;

import com.example.Uber.dto.PassengerRequest;
import com.example.Uber.dto.PassengerResponse;
import com.example.Uber.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
public class PassengerController {
    
    private final PassengerService passengerService;
    
    @GetMapping
    public ResponseEntity<List<PassengerResponse>> getAllPassengers() {
        List<PassengerResponse> passengers = passengerService.findAll();
        return ResponseEntity.ok(passengers);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        return passengerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<PassengerResponse> getPassengerByEmail(@PathVariable String email) {
        return passengerService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@Valid @RequestBody PassengerRequest request) {
        try {
            PassengerResponse passenger = passengerService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(passenger);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> updatePassenger(
            @PathVariable Long id,
            @Valid @RequestBody PassengerRequest request) {
        try {
            PassengerResponse passenger = passengerService.update(id, request);
            return ResponseEntity.ok(passenger);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        try {
            passengerService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

