package com.smartavaas.repository;


import com.smartavaas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobile(String mobile);
    Optional<User> findByEmail(String email);
    Optional<User> findByMobileOrEmail(String mobile, String email);

}


//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//    boolean existsByEmail(String email);
//
//    void deleteByEmail(String email);
//
//    Optional<User> findByEmail(String email);
//
//
//}
