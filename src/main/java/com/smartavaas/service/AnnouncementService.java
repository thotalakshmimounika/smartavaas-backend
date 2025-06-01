package com.smartavaas.service;

import com.smartavaas.model.Announcement;
import com.smartavaas.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public List<Announcement> getTopOpenAnnouncements() {
        return announcementRepository.findTop15ByStatusOrderByCreatedAtDesc(Announcement.Status.OPEN);
    }

    public Announcement createAnnouncement(String description, int duration) {
        Announcement a = new Announcement();
        a.setDescription(description);
        a.setDurationInMinutes(duration);
        return announcementRepository.save(a);
    }

    // Background job to close expired announcements
    @Scheduled(fixedRate = 60000) // every minute
    public void closeExpiredAnnouncements() {
        LocalDateTime now = LocalDateTime.now();
        List<Announcement> openList = announcementRepository.findAllByStatus(Announcement.Status.OPEN);
        for (Announcement a : openList) {
            if (a.getCreatedAt().plusMinutes(a.getDurationInMinutes()).isBefore(now)) { //Is createdAt + duration before current time
                a.setStatus(Announcement.Status.CLOSED);
                a.setModifiedAt(now);
                announcementRepository.save(a);
            }
        }
    }
}

