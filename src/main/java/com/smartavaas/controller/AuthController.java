package com.smartavaas.controller;

import com.smartavaas.dto.BaseApiResponse;
import com.smartavaas.dto.CheckEmailRequest;
import com.smartavaas.dto.OtpRequest;
import com.smartavaas.model.User;
import com.smartavaas.repository.UserRepository;
import com.smartavaas.security.JwtUtil;
import com.smartavaas.service.OtpService;
import com.smartavaas.service.UserService;
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

    @PostMapping("/login")
    public ResponseEntity<BaseApiResponse<Map<String, Object>>> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        if (userService.authenticateUser(email, password)) {
            User user = userRepository.findByMobileOrEmail(email, email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Map<String, Object> responseData = Map.of(
                    "token", jwtUtil.generateToken(user.getEmail()),
                    "fullname", user.getFirstname() + " " + user.getLastname(),
                    "role", user.getRoles(),
                    "userId", user.getId()
            );

            return ResponseEntity.ok(BaseApiResponse.<Map<String, Object>>builder()
                    .timestamp(LocalDateTime.now())
                    .statusCode(200)
                    .status("success")
                    .message("Login successful")
                    .data(responseData)
                    .build());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                BaseApiResponse.<Map<String, Object>>builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(401)
                        .status("fail")
                        .message("Invalid credentials")
                        .data(null)
                        .build());
    }

    @PostMapping("/send-otp")
    public ResponseEntity<BaseApiResponse<Map<String, String>>> sendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        try {
            boolean sent = otpService.sendOtpToEmail(email);
            HttpStatus status = sent ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;

            return new ResponseEntity<>(BaseApiResponse.<Map<String, String>>builder()
                    .timestamp(LocalDateTime.now())
                    .statusCode(status.value())
                    .status(sent ? "success" : "fail")
                    .message(sent ? "OTP sent successfully" : "Failed to send OTP")
                    .data(Map.of("email", email))
                    .build(), status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    BaseApiResponse.<Map<String, String>>builder()
                            .timestamp(LocalDateTime.now())
                            .statusCode(500)
                            .status("error")
                            .message("Unexpected error while sending OTP: " + e.getMessage())
                            .data(Map.of("email", email))
                            .build());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<BaseApiResponse<Map<String, String>>> verifyOtp(@RequestBody OtpRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();

        try {
            boolean isValid = otpService.verifyOtp(email, otp);
            if (isValid) {
                return ResponseEntity.ok(BaseApiResponse.<Map<String, String>>builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(200)
                        .status("success")
                        .message("OTP verified successfully")
                        .data(Map.of("jwt", jwtUtil.generateToken(email), "email", email))
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        BaseApiResponse.<Map<String, String>>builder()
                                .timestamp(LocalDateTime.now())
                                .statusCode(401)
                                .status("fail")
                                .message("Invalid OTP")
                                .data(Map.of("email", email))
                                .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    BaseApiResponse.<Map<String, String>>builder()
                            .timestamp(LocalDateTime.now())
                            .statusCode(500)
                            .status("error")
                            .message("Exception during OTP verification: " + e.getMessage())
                            .data(Map.of("email", email))
                            .build());
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
