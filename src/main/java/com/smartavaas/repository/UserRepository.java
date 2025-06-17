package com.smartavaas.repository;


import com.smartavaas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobile(String mobile);
    Optional<User> findByEmail(String email);
    Optional<User> findByMobileOrEmail(String mobile, String email);
    Boolean existsByEmail(String email);
}

