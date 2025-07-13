package com.smartavaas.repository;

import com.smartavaas.constants.AmenityStatus;
import com.smartavaas.model.Amenity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenitiesRepository extends JpaRepository<Amenity, Long> {
    List<Amenity> findByStatus(AmenityStatus status);
    void deleteById(Long bookingId);
}