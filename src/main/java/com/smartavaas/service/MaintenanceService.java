package com.smartavaas.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartavaas.dto.MaintenanceRequestDto;
import com.smartavaas.dto.MaintenanceResponseDto;
import com.smartavaas.model.Maintenance;
import com.smartavaas.repository.MaintenanceRepository;
import com.smartavaas.repository.UserRepository;

@Service
public class MaintenanceService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MaintenanceRepository maintenanceRepository;

    public boolean createRequest(long userId, MaintenanceRequestDto maintenanceRequestDto) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        Maintenance maintenance = new Maintenance();
        maintenance.setTitle(maintenanceRequestDto.getTitle());
        maintenance.setDescription(maintenanceRequestDto.getDescription());
        maintenance.setUserId(userId);
        maintenance.setCreatedAt(LocalDateTime.now());
        maintenance.setModifiedAt(LocalDateTime.now());
        maintenance.setStatus("pending");
        maintenanceRepository.save(maintenance);
        return true;
    }
    public List<MaintenanceResponseDto> getRequestsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        List<Maintenance> maintenances = maintenanceRepository.findByUserId(userId);
        return maintenances.stream()
            .map(maintenance -> {
                MaintenanceResponseDto responseDto = new MaintenanceResponseDto();
                responseDto.setUserid(maintenance.getUserId());
                responseDto.setTitle(maintenance.getTitle());
                responseDto.setDescription(maintenance.getDescription());
                responseDto.setCreatedAt(maintenance.getCreatedAt());
                responseDto.setModifiedAt(maintenance.getModifiedAt());
                responseDto.setStatus(maintenance.getStatus());
                return responseDto;
            })
            .toList();
    }
    public boolean updateRequestByUser(long userId, Long requestId, MaintenanceRequestDto maintenanceRequestDto) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        Maintenance maintenance = maintenanceRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Maintenance request not found"));

        if (maintenance.getUserId() != userId) {
            throw new RuntimeException("You can only update your own requests");
        }

        maintenance.setTitle(maintenanceRequestDto.getTitle());
        maintenance.setDescription(maintenanceRequestDto.getDescription());
        maintenance.setModifiedAt(LocalDateTime.now());
        maintenanceRepository.save(maintenance);
        return true;
    }

    public boolean updateRequestStatusByAdmin(Long requestId, String status) {
        Maintenance maintenance = maintenanceRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Maintenance request not found"));

        if (!status.equals("pending") && !status.equals("in-progress") && !status.equals("completed")) {
            throw new RuntimeException("Invalid status");
        }

        maintenance.setStatus(status);
        maintenance.setModifiedAt(LocalDateTime.now());
        maintenanceRepository.save(maintenance);
        return true;
    }

    public boolean deleteRequest(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        Maintenance maintenance = maintenanceRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Maintenance request not found"));
        maintenanceRepository.delete(maintenance);
        return true;
    }



}
