package com.smartavaas.init;

import com.smartavaas.model.RoleType;
import com.smartavaas.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void checkRolesExist() {
        for (RoleType roleType : RoleType.values()) {
            boolean exists = roleRepository.findByName(roleType).isPresent();
            if (!exists) {
                System.err.println("⚠️ MISSING ROLE in DB: " + roleType);
            }
        }
    }
}