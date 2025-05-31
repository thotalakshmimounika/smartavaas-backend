package com.smartavaas.controller;

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
import java.util.Map;

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

//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody User user) {
//        userService.registerUser(user);
//        return ResponseEntity.ok("User registered successfully.");
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String mobile = loginData.get("mobile");
        String password = loginData.get("password");

        System.out.println("LOGIN ATTEMPT: " + mobile + " / " + password);

        boolean isValid = userService.authenticateUser(mobile, password);
        if (isValid) {
            User user = userRepository.findByMobileOrEmail(mobile, mobile)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = jwtUtil.generateToken(user);
            String firstname = user.getFirstname();
            String lastname = user.getLastname();
            return ResponseEntity.ok(Map.of("token", token,
                    "fullname", firstname+" "+lastname));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }






    /**
     * Request OTP to be sent to the user identifier (phone or email).
     *
     * @param identifier phone number or email of the user
     * @return ResponseEntity with a message indicating OTP sent
     */
    @PostMapping("/request-otp")
    public ResponseEntity<String> requestOtp(@RequestParam String identifier) {
        String otp = authService.generateOtp(identifier);
        // In real app, OTP would be sent via SMS or email.
        return ResponseEntity.ok("OTP sent to " + identifier);
    }

    /**
     * Verify the OTP provided by the user.
     *
     * @param identifier phone number or email
     * @param otp OTP code to verify
     * @return ResponseEntity with success or failure message
     */

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
