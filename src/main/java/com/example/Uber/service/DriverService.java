package com.example.Uber.service;

/**
 * Main service interface for Driver operations
 * Extends both read and write interfaces
 * Following Dependency Inversion Principle
 */
public interface DriverService extends DriverReadService, DriverWriteService {
}

