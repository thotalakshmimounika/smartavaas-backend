package com.smartavaas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class OtpSendResponse {
    private LocalDateTime timestamp;   // When the response was generated
    private int statusCode;            // 200, 500, etc.
    private String status;             // "success", "fail", "error"
    private String message;            // Human-readable message
    private Map<String, Object> data;  // Extra info, e.g. { "email": "user@example.com" }
}
