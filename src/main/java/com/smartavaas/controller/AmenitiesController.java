package com.smartavaas.controller;

import com.smartavaas.common.ApiResponseBuilder;
import com.smartavaas.constants.AmenityStatus;
import com.smartavaas.dto.AmenityRequestDTO;
import com.smartavaas.dto.AmenityResponse;
import com.smartavaas.dto.AmenityBookingResponse;
import com.smartavaas.dto.AmenitiesBookingRequestDto;
import com.smartavaas.dto.BaseApiResponse;
import com.smartavaas.model.Amenity;
import com.smartavaas.model.AmenitiesBooking;
import com.smartavaas.service.AmenitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/amenities")
public class AmenitiesController {

    @Autowired
    private AmenitiesService amenitiesService;

    @GetMapping()
    public ResponseEntity<BaseApiResponse<Object>> getAllAmenities() {
        try {
            List<AmenityResponse> amenities = amenitiesService.getAllAmenities();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponseBuilder.success("Amenities retrieved successfully", amenities));
        } catch (Exception ex) {
            ex.printStackTrace();

            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", ex.getMessage());
            errorDetails.put("stackTrace", Arrays.toString(ex.getStackTrace()));

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseBuilder.error(
                            "An unexpected error occurred",
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            errorDetails
                    ));
        }
    }
    //get amenity by id
    @GetMapping("/{id}")
    public ResponseEntity<BaseApiResponse<AmenityResponse>> getAmenityById(@PathVariable Long id) {
        try {
            AmenityResponse amenity = amenitiesService.getAmenityById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponseBuilder.success("Amenity retrieved successfully", amenity));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseBuilder.error("Failed to retrieve amenity: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, (AmenityResponse) Map.of("errorDetails", ex.getStackTrace())));
        }
    }

    @PostMapping()
    public ResponseEntity<BaseApiResponse<Map<String, Object>>> addAmenity(@RequestBody AmenityRequestDTO amenityRequestDTO) {
        try {
            Map<String, Object> response = amenitiesService.addAmenity(amenityRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseBuilder.success("Amenity added successfully", response));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseBuilder.error("Failed to add amenity: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, Map.of("errorDetails", ex.getStackTrace())));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseApiResponse<AmenityResponse>> updateAmenity(@PathVariable Long id, @RequestBody AmenityRequestDTO updatedAmenity) {
        try {
            AmenityResponse amenity = amenitiesService.updateAmenity(id, updatedAmenity);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponseBuilder.success("Amenity updated successfully", amenity));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseBuilder.error("Failed to update amenity: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, (AmenityResponse) Map.of("errorDetails", ex.getStackTrace())));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseApiResponse<Void>> deleteAmenity(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponseBuilder.success("Amenity deleted successfully", null));
    }

}