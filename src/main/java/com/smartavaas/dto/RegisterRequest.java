package com.smartavaas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {

    @NotBlank(message = "First Name is required")
    private String firstname;

    @NotBlank(message = "Last Name is required")
    private String lastname;

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

//    // Optional field - only needed if admin wants to assign custom roles
//    private Set<String> roles;
}
