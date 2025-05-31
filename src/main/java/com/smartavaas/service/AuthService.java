package com.smartavaas.service;

import com.smartavaas.dto.RegisterRequest;
import com.smartavaas.model.User;
import com.smartavaas.repository.RoleRepository;
import com.smartavaas.repository.UserRepository;
import com.smartavaas.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;
    private final Map<String, String> otpStorage = new HashMap<>();

    //user.setRoles(Set.of(userRole));

    //  Register with default role

    public void register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign roles based on request
        if (request.getRoles() != null && request.getRoles().contains("ROLE_ADMIN")) {
            user.setRoles(roleService.getAdminRoles());
        } else {
            user.setRoles(roleService.getDefaultUserRoles());
        }

        userRepository.save(user);
    }



    public String generateOtp(String identifier) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
        otpStorage.put(identifier, otp);

        // Log OTP (simulate SMS/email)
        System.out.println("Generated OTP for " + identifier + ": " + otp);
        return otp;
    }

    public boolean verifyOtp(String identifier, String otp) {
        return otp.equals(otpStorage.get(identifier));
    }

    public String authenticateUser(String identifier) {
        User user = userRepository.findByMobileOrEmail(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Generate JWT after successful OTP verification
        return jwtUtil.generateToken(user);
    }
}
