package com.smartavaas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BaseApiResponse<T> {
    private LocalDateTime timestamp;
    private int statusCode;
    private String status;   // "success", "fail", "error"
    private String message;
    private T data;
}
