package com.smartavaas.dto;

import com.smartavaas.constants.AmenityStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AmenityBookingResponse {
    private Long id;
    private Long userId;
    private Long amenityId;
    private Integer duration;
    private Integer countOfPeople;
    private LocalDateTime dateOfBooking;
}