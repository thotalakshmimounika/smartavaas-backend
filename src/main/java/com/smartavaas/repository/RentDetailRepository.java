package com.smartavaas.repository;

import com.smartavaas.model.RentDetail;
import com.smartavaas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentDetailRepository extends JpaRepository<RentDetail, Long> {
    List<RentDetail> findByUser(User user);
}