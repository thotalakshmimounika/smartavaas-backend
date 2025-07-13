package com.smartavaas.repository;

import com.smartavaas.constants.AmenityStatus;
import com.smartavaas.model.AmenitiesBooking;
import com.smartavaas.model.Maintenance;
import com.smartavaas.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AmenitiesBookingRepository extends JpaRepository<AmenitiesBooking, Long> {
    List<AmenitiesBooking> findByUser(User user);
    void deleteById(Long bookingId);

    @Query("""
            SELECT ab FROM AmenitiesBooking ab
            WHERE ab.amenity.id = :amenityId
              AND :now BETWEEN ab.BookingStartOn AND ab.BookingEndOn
            """)
    List<AmenitiesBooking> findActiveBookingsByAmenityId(
            @Param("amenityId") Long amenityId,
            @Param("now") LocalDateTime now
    );

    @Query("""
                SELECT ab FROM AmenitiesBooking ab
                WHERE ab.amenity.id = :amenityId
                  AND (
                    (:start < ab.BookingEndOn AND :end > ab.BookingStartOn)
                  )
            """)
    List<AmenitiesBooking> findConflictingBookings(
            @Param("amenityId") Long amenityId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}