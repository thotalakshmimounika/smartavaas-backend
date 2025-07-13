package com.smartavaas.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AmenitiesBookingRequestDto {

    @NotNull(message = "Amenity ID is required")
    private Long amenityId;

    @NotNull(message = "Booking start time is required")
    @Future(message = "Booking start time must be in the future")
    private LocalDateTime bookingStartOn;

    //duration in mintues
    @NotNull(message = "Booking duration is required")
    private Integer bookingDuration;

    @NotNull(message = "Count of people is required")
    private Integer countOfPeople;


}