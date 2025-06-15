package com.smartavaas.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.smartavaas.dto.BaseApiResponse;
import com.smartavaas.exception.InvalidDataException;
import com.smartavaas.exception.MaintenanceNotFoundException;
import com.smartavaas.exception.UserNotFoundException;

@ControllerAdvice
public class ResidentCommunityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<BaseApiResponse<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        BaseApiResponse<Object> response = BaseApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .status("failed")
                .message(ex.getMessage())
                .data(ex.getClass().getName())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MaintenanceNotFoundException.class)
    public ResponseEntity<BaseApiResponse<Object>> handleMaintenanceNotFoundException(UserNotFoundException ex) {
        BaseApiResponse<Object> response = BaseApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .status("failed")
                .message(ex.getMessage())
                .data(ex.getClass().getName())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<BaseApiResponse<Object>> handleInvalidDataException(InvalidDataException ex) {
        BaseApiResponse<Object> response = BaseApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .status("failed")
                .message(ex.getMessage())
                .data(ex.getClass().getName())
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}
