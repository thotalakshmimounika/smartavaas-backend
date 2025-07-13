package com.smartavaas.model;

import com.smartavaas.constants.AmenityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

@Entity
@Table(name = "amenities_booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmenitiesBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private int BookingDuration;

    @Column(nullable = false)
    private int countOfPeople;

    @Column(nullable = false)
    private LocalDateTime BookingStartOn;

    @Column(nullable = false)
    private LocalDateTime BookingEndOn;

    @ManyToOne
    @JoinColumn(name = "amenityId", nullable = false)
    private Amenity amenity;

}