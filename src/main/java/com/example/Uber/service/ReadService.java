package com.example.Uber.service;

import java.util.List;
import java.util.Optional;

/**
 * Generic interface for read operations
 * Following Interface Segregation Principle
 */
public interface ReadService<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
}

