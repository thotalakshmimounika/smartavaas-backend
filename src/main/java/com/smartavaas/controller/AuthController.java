package com.smartavaas.controller;

import com.smartavaas.dto.OtpRequest;
import com.smartavaas.dto.OtpSendResponse;
import com.smartavaas.dto.OtpVerifyResponse;
import com.smartavaas.model.Role;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");
        System.out.println("LOGIN ATTEMPT: " + email + " / " + password);
        boolean isValid = userService.authenticateUser(email, password);
        if (isValid) {
            User user = userRepository.findByMobileOrEmail(email, email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = jwtUtil.generateToken(user.getEmail());
            String firstname = user.getFirstname();
            String lastname = user.getLastname();
            Set<Role> role = new HashSet<>();
            role = user.getRoles();
            return ResponseEntity.ok(Map.of("token", token,
                    "fullname", firstname+" "+lastname,
                    "role",role,
                    "userId",user.getId()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "fail", "message", "Invalid credentials"));
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<OtpSendResponse> sendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        try {
            boolean sent = otpService.sendOtpToEmail(email);
            OtpSendResponse response = OtpSendResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .statusCode(sent ? 200 : 500)
                    .status(sent ? "success" : "fail")
                    .message(sent ? "OTP sent successfully to the provided email." : "Failed to send OTP. Please try again.")
                    .data(Map.of("email", email))
                    .build();
            return new ResponseEntity<>(response,
                    sent ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            OtpSendResponse response = OtpSendResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .statusCode(500)
                    .status("error")
                    .message("Unexpected error occurred while sending OTP: " + e.getMessage())
                    .data(Map.of("email", email))
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<OtpVerifyResponse> verifyOtp(@RequestBody OtpRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();

        try {
            boolean isValid = otpService.verifyOtp(email, otp);

            if (isValid) {
                String token = jwtUtil.generateToken(email);
                OtpVerifyResponse response = OtpVerifyResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(200)
                        .status("success")
                        .message("OTP verified successfully.")
                        .data(Map.of("jwt", token, "email", email))
                        .build();

                return ResponseEntity.ok(response);
            } else {
                OtpVerifyResponse response = OtpVerifyResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(401)
                        .status("fail")
                        .message("Invalid OTP.")
                        .data(Map.of("email", email))
                        .build();

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            OtpVerifyResponse response = OtpVerifyResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .statusCode(500)
                    .status("error")
                    .message("Exception during OTP verification: " + e.getMessage())
                    .data(Map.of("email", email))
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
