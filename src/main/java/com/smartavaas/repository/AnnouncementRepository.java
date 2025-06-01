package com.smartavaas.repository;

import com.smartavaas.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findTop15ByStatusOrderByCreatedAtDesc(Announcement.Status status);
    List<Announcement> findAllByStatus(Announcement.Status status);
}

