package com.smartavaas.service;

import com.smartavaas.dto.RegisterRequest;
import com.smartavaas.dto.RegisterResponse;
import com.smartavaas.model.Role;
import com.smartavaas.model.RoleType;
import com.smartavaas.model.User;
import com.smartavaas.repository.RoleRepository;
import com.smartavaas.repository.UserRepository;
import com.smartavaas.exception.DuplicateEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }
        Role residentRole = roleRepository.findByName(RoleType.ROLE_RESIDENT)
                .orElseThrow(() -> new RuntimeException("ROLE_RESIDENT not found in DB"));
        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(residentRole));

        userRepository.save(user);

        emailService.sendVerificationCode(user.getEmail(), "Welcome to ResiAssist!");

        return new RegisterResponse(user.getId(), user.getEmail(), "User registered successfully");
    }

    public boolean authenticateUser(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

}
