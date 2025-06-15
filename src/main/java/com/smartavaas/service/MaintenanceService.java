package com.smartavaas.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartavaas.dto.MaintenanceRequestDto;
import com.smartavaas.dto.MaintenanceResponseDto;
import com.smartavaas.exception.InvalidDataException;
import com.smartavaas.exception.MaintenanceNotFoundException;
import com.smartavaas.exception.UserNotFoundException;
import com.smartavaas.model.Maintenance;
import com.smartavaas.model.User;
import com.smartavaas.repository.MaintenanceRepository;
import com.smartavaas.repository.UserRepository;

@Service
public class MaintenanceService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MaintenanceRepository maintenanceRepository;

    public Map<String, Object> createRequest(long userId, MaintenanceRequestDto maintenanceRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Error in creating maintenance request for user" + userId + "Invalid userId, User not found"));

        Maintenance maintenance = new Maintenance();
        maintenance.setTitle(maintenanceRequestDto.getTitle());
        maintenance.setDescription(maintenanceRequestDto.getDescription());
        maintenance.setUser(user);
        maintenance.setCreatedAt(LocalDateTime.now());
        maintenance.setModifiedAt(LocalDateTime.now());
        maintenance.setStatus("pending");
        maintenanceRepository.save(maintenance);

        return Map.of(
                "email", user.getEmail(),
                "userId", user.getId(),
                "title", maintenance.getTitle(),
                "description", maintenance.getDescription()
        );
    }

    public Map<String, Object> getRequestsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Error in fetching maintenance request for user " + userId + ". Invalid userId, User not found"));

        List<Map<String, Object>> requests = maintenanceRepository.findByUser(user).stream()
                .map(maintenance -> {
                    Map<String, Object> requestMap = new HashMap<>();
                    requestMap.put("requestId", maintenance.getId());
                    requestMap.put("title", maintenance.getTitle());
                    requestMap.put("description", maintenance.getDescription());
                    requestMap.put("createdAt", maintenance.getCreatedAt());
                    requestMap.put("modifiedAt", maintenance.getModifiedAt());
                    requestMap.put("status", maintenance.getStatus());
                    return requestMap;
                })
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("requests", requests);

        return response;
    }
    public void checkAndSetString(Consumer<String> target, Supplier<String> source) {
        String value = source.get();
        if (value != null && !value.isBlank()) {
            target.accept(value);
        }
    }

    public Map<String, Object> updateRequestByUser(long userId, Long requestId, MaintenanceRequestDto maintenanceRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Error in updating maintenance request for user" + userId + "Invalid userId, User not found"));

        Maintenance maintenance = maintenanceRepository.findById(requestId)
                .orElseThrow(() -> new MaintenanceNotFoundException("Error in updating maintenance request for user " + userId + "having" + requestId + "Invalid requestId, request not found"));

        if (!maintenance.getUser().getId().equals(user.getId())) {
            throw new InvalidDataException("You can only update your own requests");
        }
        checkAndSetString(maintenance::setTitle, maintenanceRequestDto::getTitle);
        checkAndSetString(maintenance::setDescription, maintenanceRequestDto::getDescription);
        maintenance.setModifiedAt(LocalDateTime.now());
        maintenanceRepository.save(maintenance);

        return Map.of(
                "email", user.getEmail(),
                "userId", user.getId(),
                "requestId", requestId,
                "title", maintenance.getTitle(),
                "description", maintenance.getDescription()
        );
    }

    public boolean updateRequestStatusByAdmin(Long requestId, String status) {
        Maintenance maintenance = maintenanceRepository.findById(requestId)
                .orElseThrow(() -> new MaintenanceNotFoundException("Error in updating maintenance request " + requestId + "Invalid requestId, request not found"));

        if (!status.equals("pending") && !status.equals("in-progress") && !status.equals("completed")) {
            throw new InvalidDataException("Invalid status");
        }

        maintenance.setStatus(status);
        maintenance.setModifiedAt(LocalDateTime.now());
        maintenanceRepository.save(maintenance);
        return true;
    }

    public Map<String, Object> deleteRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Error in deleting maintenance request for user" + userId + "Invalid userId, User not found"));

        Maintenance maintenance = maintenanceRepository.findById(requestId)
                .orElseThrow(() -> new MaintenanceNotFoundException("Error in deleting maintenance request for user " + userId + "having" + requestId + "Invalid requestId, request not found"));

        if (!maintenance.getUser().getId().equals(user.getId())) {
            throw new InvalidDataException("You can only delete your own requests");
        }

        maintenanceRepository.delete(maintenance);

        return Map.of(
                "email", user.getEmail(),
                "userId", user.getId(),
                "requestId", requestId,
                "title", maintenance.getTitle(),
                "description", maintenance.getDescription()
        );
    }
}