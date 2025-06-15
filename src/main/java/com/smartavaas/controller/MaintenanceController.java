package com.smartavaas.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartavaas.dto.BaseApiResponse;
import com.smartavaas.dto.MaintenanceRequestDto;
import com.smartavaas.dto.MaintenanceResponseDto;
import com.smartavaas.service.MaintenanceService;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {
    @Autowired
    private MaintenanceService maintenanceService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<BaseApiResponse<Map<String, Object>>> createRequest(
            @PathVariable long userId,
            @RequestBody MaintenanceRequestDto maintenanceRequestDto) {
        Map<String, Object> responseData = maintenanceService.createRequest(userId, maintenanceRequestDto);
        return ResponseEntity.ok(BaseApiResponse.<Map<String, Object>>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(200)
                .status("success")
                .message("Request created successfully")
                .data(responseData)
                .build());
    }

    @GetMapping("/track/{userId}")
    public ResponseEntity<BaseApiResponse<Map<String, Object>>> trackRequests(@PathVariable Long userId) {
        Map<String, Object> responseData = maintenanceService.getRequestsByUserId(userId);
        return ResponseEntity.ok(BaseApiResponse.<Map<String, Object>>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(200)
                .status("success")
                .message("Requests retrieved successfully")
                .data(responseData)
                .build());
    }
    @PatchMapping("/user/update/{userId}/{requestId}")
    public ResponseEntity<BaseApiResponse<Map<String, Object>>> updateRequestByUser(
            @PathVariable long userId,
            @PathVariable Long requestId,
            @RequestBody MaintenanceRequestDto maintenanceRequestDto) {
        Map<String, Object> responseData = maintenanceService.updateRequestByUser(userId, requestId, maintenanceRequestDto);
        return ResponseEntity.ok(BaseApiResponse.<Map<String, Object>>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(200)
                .status("success")
                .message("Request updated successfully")
                .data(responseData)
                .build());
    }

    @PatchMapping("/admin/update-status/{requestId}")
    public ResponseEntity<BaseApiResponse<Map<String, Object>>> updateRequestByAdmin(
            @PathVariable Long requestId,
            @RequestBody String status) {
        boolean updated = maintenanceService.updateRequestStatusByAdmin(requestId, status);
        if (updated) {
            Map<String, Object> responseData = Map.of(
                    "message", "Request status updated successfully.",
                    "requestId", requestId,
                    "status", status
            );
            return ResponseEntity.ok(BaseApiResponse.<Map<String, Object>>builder()
                    .timestamp(LocalDateTime.now())
                    .statusCode(200)
                    .status("success")
                    .message("Request status updated successfully")
                    .data(responseData)
                    .build());
        } else {
            Map<String, Object> errorData = Map.of(
                    "message", "Request not found or update failed.",
                    "requestId", requestId
            );
            return ResponseEntity.status(404).body(BaseApiResponse.<Map<String, Object>>builder()
                    .timestamp(LocalDateTime.now())
                    .statusCode(404)
                    .status("error")
                    .message("Request status update failed")
                    .data(errorData)
                    .build());
        }
    }

    @DeleteMapping("/delete/{userId}/{requestId}")
    public ResponseEntity<BaseApiResponse<Map<String, Object>>> deleteRequest(
            @PathVariable long userId,
            @PathVariable Long requestId) {
        Map<String, Object> responseData = maintenanceService.deleteRequest(userId, requestId);
        return ResponseEntity.ok(BaseApiResponse.<Map<String, Object>>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(200)
                .status("success")
                .message("Request deleted successfully")
                .data(responseData)
                .build());
    }

}

