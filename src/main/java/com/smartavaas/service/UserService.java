package com.smartavaas.service;


import com.smartavaas.model.User;
import com.smartavaas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<?> registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("status", "fail", "message", "Email already exists"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("status", "success", "message", "User registered successfully"));

    }

    public boolean authenticateUser(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

}
