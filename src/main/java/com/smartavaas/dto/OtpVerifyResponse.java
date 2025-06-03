package com.smartavaas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class OtpVerifyResponse {
    private LocalDateTime timestamp;
    private int statusCode;
    private String status;        // "success", "fail", "error"
    private String message;
    private Map<String, Object> data; // Can include JWT, email, role, etc.
}
