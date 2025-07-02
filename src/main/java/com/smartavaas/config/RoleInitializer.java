package com.smartavaas.config;

import com.smartavaas.model.Role;
import com.smartavaas.model.RoleType;
import com.smartavaas.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
Runs once after Spring Boot starts.
Iterates over all RoleType enum values.
Checks if each role already exists in the DB.
If not found, saves it to the database.
 */
@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;
    private static final Logger log = LoggerFactory.getLogger(RoleInitializer.class);

    @PostConstruct
    public void initRoles() {
        Arrays.stream(RoleType.values()).forEach(roleType -> {
            roleRepository.findByName(roleType).orElseGet(() -> {
                Role newRole = new Role(roleType);
                log.info("Creating missing role: {}", roleType);
                return roleRepository.save(newRole);
            });
        });
    }
}
