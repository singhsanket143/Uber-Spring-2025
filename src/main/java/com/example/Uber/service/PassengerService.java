package com.example.Uber.service;

/**
 * Main service interface for Passenger operations
 * Extends both read and write interfaces
 * Following Dependency Inversion Principle
 */
public interface PassengerService extends PassengerReadService, PassengerWriteService {
}

