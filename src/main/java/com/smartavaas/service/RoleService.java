package com.smartavaas.service;

import com.smartavaas.model.Role;
import com.smartavaas.model.RoleType;
import com.smartavaas.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRoleByName(RoleType roleType) {
        return roleRepository.findByName(roleType)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleType));
    }

    public Set<Role> getDefaultUserRoles() {
        return Set.of(getRoleByName(RoleType.ROLE_USER));
    }

    public Set<Role> getAdminRoles() {
        return Set.of(getRoleByName(RoleType.ROLE_ADMIN));
    }
}
