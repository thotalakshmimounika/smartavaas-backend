package com.smartavaas.controller;

import com.smartavaas.common.ApiResponseBuilder;
import com.smartavaas.dto.*;
import com.smartavaas.exception.InvalidCredentialsException;
import com.smartavaas.model.User;
import com.smartavaas.repository.UserRepository;
import com.smartavaas.security.JwtUtil;
import com.smartavaas.service.AuthService;
import com.smartavaas.service.OtpService;
import com.smartavaas.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private OtpService otpService;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserService userService;
    @Autowired private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseApiResponse<AuthResponse>> login(@RequestBody @Valid AuthRequest request) {
        try {
            AuthResponse data = authService.login(request);
            return ResponseEntity.ok(ApiResponseBuilder.success("Login successful", data));
        } catch (InvalidCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseBuilder.failure(ex.getMessage(), HttpStatus.UNAUTHORIZED));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseBuilder.failure("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<BaseApiResponse<Map<String, String>>> sendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        try {
            boolean sent = otpService.sendOtpToEmail(email);
            HttpStatus status = sent ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;

            return ResponseEntity.status(status).body(
                    sent
                            ? ApiResponseBuilder.success("OTP sent successfully", Map.of("email", email))
                            : ApiResponseBuilder.failure("Failed to send OTP", status)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponseBuilder.error("Unexpected error while sending OTP: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, Map.of("email", email))
            );
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<BaseApiResponse<Map<String, Object>>> verifyOtp(@RequestBody OtpRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();

        try {
            boolean isValid = otpService.verifyOtp(email, otp);
            if (isValid) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

                Map<String, Object> responseData = Map.of(
                        "token", jwtUtil.generateToken(email),
                        "email", email,
                        "fullname", user.getFirstname() + " " + user.getLastname(),
                        "userId", user.getId(),
                        "role", user.getRoles()
                );

                return ResponseEntity.ok(ApiResponseBuilder.success("OTP verified successfully", responseData));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponseBuilder.failure("Invalid OTP", HttpStatus.UNAUTHORIZED));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponseBuilder.error("Exception during OTP verification: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, Map.of("email", email))
            );
        }
    }

    @PostMapping("/check-email")
    public ResponseEntity<BaseApiResponse<Boolean>> checkEmailExists(@RequestBody CheckEmailRequest request) {
        boolean exists = userRepository.existsByEmail(request.getEmail());

        BaseApiResponse<Boolean> response = BaseApiResponse.<Boolean>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(exists ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value())
                .status(exists ? "success" : "fail")
                .message(exists ? "Email exists" : "Email not found")
                .data(exists)
                .build();

        return new ResponseEntity<>(response, exists ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }


}
