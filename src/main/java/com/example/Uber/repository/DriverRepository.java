package com.example.Uber.repository;

import com.example.Uber.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);
    Optional<Driver> findByLicenseNumber(String licenseNumber);
    boolean existsByEmail(String email);
    boolean existsByLicenseNumber(String licenseNumber);
}

