package com.smartavaas.service;

import com.smartavaas.constants.AmenityStatus;
import com.smartavaas.dto.AmenitiesBookingRequestDto;
import com.smartavaas.dto.AmenityBookingResponse;
import com.smartavaas.dto.AmenityRequestDTO;
import com.smartavaas.dto.AmenityResponse;
import com.smartavaas.exception.UserNotFoundException;
import com.smartavaas.model.AmenitiesBooking;
import com.smartavaas.model.Amenity;
import com.smartavaas.model.User;
import com.smartavaas.repository.AmenitiesBookingRepository;
import com.smartavaas.repository.AmenitiesRepository;
import com.smartavaas.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AmenitiesService {

    @Autowired
    private AmenitiesRepository amenitiesRepository;

    @Autowired
    private AmenitiesBookingRepository amenitiesBookingRepository;

    @Autowired
    private UserRepository userRepository;

    // Amenity-related methods
    public List<AmenityResponse> getAllAmenities() {
        List<Amenity> amenities = amenitiesRepository.findAll();
        return amenities.stream().map(amenity -> {
            AmenityResponse response = new AmenityResponse();
            response.setId(amenity.getId());
            response.setName(amenity.getName());
            response.setDescription(amenity.getDescription());
            response.setStatus(amenity.getStatus());
            return response;
        }).toList();
    }
    public AmenityResponse getAmenityById(Long id) {
        Amenity amenity = amenitiesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid amenityId: Amenity not found"));
        AmenityResponse response = new AmenityResponse();
        response.setId(amenity.getId());
        response.setName(amenity.getName());
        response.setDescription(amenity.getDescription());
        response.setStatus(amenity.getStatus());
        return response;
    }

    public Map<String, Object> addAmenity(AmenityRequestDTO amenityRequestDTO) {
        Amenity amenity = new Amenity();
        amenity.setName(amenityRequestDTO.getName());
        amenity.setDescription(amenityRequestDTO.getDescription());
        amenity.setStatus(AmenityStatus.AVAILABLE);
        amenity.setCreatedAt(LocalDateTime.now());
        amenity.setModifiedAt(LocalDateTime.now());
        amenitiesRepository.save(amenity);
        return Map.of("amenityId", amenity.getId(),"Amenity name", amenity.getName());
    }

    public AmenityResponse updateAmenity(Long id, AmenityRequestDTO updatedAmenity) {
        Amenity amenity = amenitiesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid amenityId: Amenity not found"));
        amenity.setName(updatedAmenity.getName());
        amenity.setDescription(updatedAmenity.getDescription());
        amenity.setModifiedAt(LocalDateTime.now());
        amenitiesRepository.save(amenity);
        AmenityResponse response = new AmenityResponse();
        response.setId(amenity.getId());
        response.setName(amenity.getName());
        response.setDescription(amenity.getDescription());
        response.setStatus(amenity.getStatus());
        return response;
    }

    public void deleteAmenity(Long id) {
        Amenity amenity = amenitiesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid amenityId: Amenity not found"));
        amenitiesRepository.delete(amenity);
    }

    // Booking-related methods
    public AmenityBookingResponse createBookingAmenities(long userId, AmenitiesBookingRequestDto bookingRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Error in booking the  amenity for user" + userId + "Invalid userId, User not found"));

        Amenity amenity = amenitiesRepository.findById(bookingRequest.getAmenityId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid amenityId: Amenity not found"));
        if (!AmenityStatus.AVAILABLE.equals(amenity.getStatus())) {
            throw new IllegalStateException("Amenity is not available for booking. Current status: " + amenity.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        AmenitiesBooking booking = new AmenitiesBooking();
        booking.setUser(user);
        booking.setAmenity(amenity);
        booking.setBookingDuration(bookingRequest.getBookingDuration());
        booking.setCountOfPeople(bookingRequest.getCountOfPeople());
        booking.setBookingStartOn(bookingRequest.getBookingStartOn());
        booking.setBookingEndOn(booking.getBookingStartOn().plusMinutes(booking.getBookingDuration()));
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);

        List<AmenitiesBooking> conflictBookings = amenitiesBookingRepository.findConflictingBookings(
                booking.getAmenity().getId(),
                booking.getBookingStartOn(),
                booking.getBookingEndOn()
        );
        if (!conflictBookings.isEmpty()) {
            throw new RuntimeException("This booking conflicts with existing bookings for the amenity.");
        }

        amenitiesBookingRepository.save(booking);
        updateAmenityStatus(amenity.getId());

        AmenityBookingResponse response = new AmenityBookingResponse();
        response.setAmenityId(amenity.getId());
        response.setUserId(user.getId());
        response.setDuration(bookingRequest.getBookingDuration());
        response.setCountOfPeople(bookingRequest.getCountOfPeople());
        response.setDateOfBooking(bookingRequest.getBookingStartOn());
        return response;
    }


    public List<AmenityBookingResponse> viewallBookings (Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Error in fetching bookings for user " + userId + ". Invalid userId, User not found"));

        List<AmenitiesBooking> bookings = amenitiesBookingRepository.findByUser(user);
        return bookings.stream().map(booking -> {
            AmenityBookingResponse response = new AmenityBookingResponse();
            response.setAmenityId(booking.getAmenity().getId());
            response.setUserId(booking.getUser().getId());
            response.setDuration(booking.getBookingDuration());
            response.setCountOfPeople(booking.getCountOfPeople());
            response.setDateOfBooking(booking.getBookingStartOn());
            return response;
        }).toList();
    }

    public Map<String, Object> cancelBooking (Long bookingId){
        AmenitiesBooking booking = amenitiesBookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bookingId: Booking not found"));

        Amenity amenity = booking.getAmenity();
        if (amenity != null) {
            amenity.setStatus(AmenityStatus.AVAILABLE);
            amenitiesRepository.save(amenity);
        }

        amenitiesBookingRepository.delete(booking);
        return Map.of("message", "Booking canceled successfully", "bookingId", bookingId);
    }

    public void updateAmenityStatus(Long amenityId) {
        LocalDateTime now = LocalDateTime.now();
        List<AmenitiesBooking> bookings = amenitiesBookingRepository.findActiveBookingsByAmenityId(amenityId, now);

        Amenity amenity = amenitiesRepository.findById(amenityId).orElseThrow();

        if (bookings.isEmpty()) {
            amenity.setStatus(AmenityStatus.AVAILABLE);
        } else {
            amenity.setStatus(AmenityStatus.UNAVAILABLE);
        }

        amenitiesRepository.save(amenity);
    }

    @Scheduled(fixedDelay = 60000)
    public void correctAmenitiesStatus() {
        LocalDateTime now = LocalDateTime.now();
        // for a current time check if the booking exists if not then set status to AVAILABLE
        // now is between bookings start and end time
        List<AmenityResponse> amenitiesList = getAllAmenities();

        for (AmenityResponse amenityResponse : amenitiesList) {
            Amenity amenity = amenitiesRepository.findById(amenityResponse.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Amenity not found with id " + amenityResponse.getId()));

            List<AmenitiesBooking> futureBookings =
                    amenitiesBookingRepository.findActiveBookingsByAmenityId(amenity.getId(), now);

            AmenityStatus newStatus = futureBookings.isEmpty()
                    ? AmenityStatus.AVAILABLE
                    : AmenityStatus.UNAVAILABLE;

            if (amenity.getStatus() != newStatus) {
                amenity.setStatus(newStatus);
                amenitiesRepository.save(amenity);
            }
        }
    }

}