
package com.smartavaas.common;

import com.smartavaas.dto.BaseApiResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponseBuilder {

    public static <T> BaseApiResponse<T> success(String message, T data) {
        return BaseApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .status("success")
                .message(message)
                .data(data)
                .build();
    }

    public static <T> BaseApiResponse<T> created(String message, T data) {
        return BaseApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.CREATED.value())
                .status("success")
                .message(message)
                .data(data)
                .build();
    }

    public static <T> BaseApiResponse<T> failure(String message, HttpStatus status) {
        return BaseApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(status.value())
                .status("fail")
                .message(message)
                .data(null)
                .build();
    }

    public static <T> BaseApiResponse<T> error(String message, HttpStatus status, T data) {
        return BaseApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(status.value())
                .status("error")
                .message(message)
                .data(data)
                .build();
    }

    public static <T> BaseApiResponse<T> found(String message, T data) {
        return BaseApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.FOUND.value())
                .status("found")
                .message(message)
                .data(data)
                .build();
    }
}
