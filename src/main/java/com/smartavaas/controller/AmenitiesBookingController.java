package com.smartavaas.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartavaas.common.ApiResponseBuilder;
import com.smartavaas.dto.AmenitiesBookingRequestDto;
import com.smartavaas.dto.AmenityBookingResponse;
import com.smartavaas.dto.BaseApiResponse;
import com.smartavaas.service.AmenitiesService;

@RestController
@RequestMapping("/amenities/bookings")
public class AmenitiesBookingController {

    @Autowired
    private AmenitiesService amenitiesService;

    @PostMapping("/{userId}")
    public ResponseEntity<BaseApiResponse<AmenityBookingResponse>> bookAmenities(
            @PathVariable long userId,
            @RequestBody AmenitiesBookingRequestDto bookingRequest) {
        try {
            AmenityBookingResponse bookingResponse = amenitiesService.createBookingAmenities(userId, bookingRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseBuilder.created("Booking created successfully", bookingResponse));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseBuilder.error("Failed to create booking: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, (AmenityBookingResponse) Map.of("errorDetails", ex.getStackTrace())));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BaseApiResponse<List<AmenityBookingResponse>>> viewBookings(@PathVariable Long userId) {
        try {
            List<AmenityBookingResponse> bookings = amenitiesService.viewallBookings(userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponseBuilder.success("Bookings retrieved successfully", bookings));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseBuilder.error("Failed to retrieve bookings: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, (List<AmenityBookingResponse>) Map.of("errorDetails", ex.getStackTrace())));
        }
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<BaseApiResponse<Map<String, Object>>> cancelBooking(@PathVariable Long bookingId) {
        try {
            Map<String, Object> response = amenitiesService.cancelBooking(bookingId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponseBuilder.success("Booking canceled successfully", response));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseBuilder.error("Failed to cancel booking: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, Map.of("errorDetails", ex.getStackTrace())));
        }
    }
}

