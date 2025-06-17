package com.smartavaas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    @NotBlank
    private String token;
    @NotBlank
    private String fullname;
    @NotBlank
    private String role;
    @NotBlank
    private Long userId;
}
