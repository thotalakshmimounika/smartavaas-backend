package com.smartavaas.controller;

import com.smartavaas.model.Announcement;
import com.smartavaas.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/getAnnouncement")
    public ResponseEntity<List<Announcement>> getAnnouncements() {
        return ResponseEntity.ok(announcementService.getTopOpenAnnouncements());
    }

    @PostMapping("/createAnnouncement")
    public ResponseEntity<Map<String, String>> createAnnouncement(@RequestBody Map<String, Object> payload) {
        String description = (String) payload.get("description");
        int duration = (int) payload.get("duration");
        announcementService.createAnnouncement(description, duration);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Announcement Created");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}
