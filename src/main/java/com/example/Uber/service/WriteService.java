package com.example.Uber.service;

/**
 * Generic interface for write operations
 * Following Interface Segregation Principle
 */
public interface WriteService<T, ID> {
    T create(T entity);
    T update(ID id, T entity);
    void deleteById(ID id);
}

