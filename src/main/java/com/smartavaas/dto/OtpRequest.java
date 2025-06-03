package com.smartavaas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequest {
    @Email(message = "Email is invalid")
    @NotBlank(message = "Email is required")
    private String email;
    private String otp;
}
