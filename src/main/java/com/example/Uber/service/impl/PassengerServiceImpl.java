package com.example.Uber.service.impl;

import com.example.Uber.dto.PassengerRequest;
import com.example.Uber.dto.PassengerResponse;
import com.example.Uber.entity.Passenger;
import com.example.Uber.mapper.PassengerMapper;
import com.example.Uber.repository.PassengerRepository;
import com.example.Uber.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PassengerServiceImpl implements PassengerService {
    
    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PassengerResponse> findById(Long id) {
        return passengerRepository.findById(id)
                .map(passengerMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PassengerResponse> findAll() {
        return passengerRepository.findAll().stream()
                .map(passengerMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PassengerResponse> findByEmail(String email) {
        return passengerRepository.findByEmail(email)
                .map(passengerMapper::toResponse);
    }
    
    @Override
    public PassengerResponse create(PassengerRequest request) {
        if (passengerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Passenger with email " + request.getEmail() + " already exists");
        }
        
        Passenger passenger = passengerMapper.toEntity(request);
        Passenger savedPassenger = passengerRepository.save(passenger);
        return passengerMapper.toResponse(savedPassenger);
    }
    
    @Override
    public PassengerResponse update(Long id, PassengerRequest request) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found with id: " + id));
        
        // Check if email is being changed and if new email already exists
        if (!passenger.getEmail().equals(request.getEmail()) && 
            passengerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Passenger with email " + request.getEmail() + " already exists");
        }
        
        passengerMapper.updateEntity(passenger, request);
        Passenger updatedPassenger = passengerRepository.save(passenger);
        return passengerMapper.toResponse(updatedPassenger);
    }
    
    @Override
    public void deleteById(Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new IllegalArgumentException("Passenger not found with id: " + id);
        }
        passengerRepository.deleteById(id);
    }
}

