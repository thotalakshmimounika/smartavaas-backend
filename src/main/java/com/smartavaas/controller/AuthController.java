package com.smartavaas.controller;

import com.smartavaas.model.Role;
import com.smartavaas.model.User;
import com.smartavaas.repository.UserRepository;
import com.smartavaas.security.JwtUtil;
import com.smartavaas.service.AuthService;
import com.smartavaas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

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

            String token = jwtUtil.generateToken(user);
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

    @PostMapping("/request-otp")
    public ResponseEntity<String> requestOtp(@RequestParam String identifier) {
        String otp = authService.generateOtp(identifier);
        // In real app, OTP would be sent via SMS or email.
        return ResponseEntity.ok("OTP sent to " + identifier);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String identifier, @RequestParam String otp) {
        boolean isValid = authService.verifyOtp(identifier, otp);
        if (isValid) {
            User user = userRepository.findByMobileOrEmail(identifier, identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = jwtUtil.generateToken(user);  // 🔥 Here
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else {
            return ResponseEntity.status(401).body("Invalid OTP. Verification failed.");
        }
    }


}
