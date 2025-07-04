package com.smartavaas.exception;

import com.smartavaas.common.ApiResponseBuilder;
import com.smartavaas.dto.BaseApiResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
//@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<BaseApiResponse<Void>> handleDuplicateEmail(DuplicateEmailException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponseBuilder.failure(ex.getMessage(), HttpStatus.CONFLICT)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseApiResponse<Void>> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponseBuilder.failure("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<BaseApiResponse<Void>> handleMessagingException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseBuilder.failure("Email Devlivery Failed. Please try Again Later",HttpStatus.INTERNAL_SERVER_ERROR)
                );
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<BaseApiResponse<Void>> handleMailException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseBuilder.failure("Unable to send due to mail server issue", HttpStatus.INTERNAL_SERVER_ERROR));
    }
}