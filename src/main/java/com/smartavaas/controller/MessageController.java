package com.smartavaas.controller;

import com.smartavaas.model.ChatMessage;
import com.smartavaas.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")

public class MessageController {

    @Autowired
    private MessageService service;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody ChatMessage msg) {
        return service.saveMessage(msg);
    }

    @PostMapping("/send-with-attachment")
    public ChatMessage sendWithAttachment(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String content,
            @RequestParam MultipartFile file) throws Exception {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        ChatMessage msg = new ChatMessage();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setContent(content);
        msg.setAttachmentPath(filePath.toString());

        return service.saveMessage(msg);
    }

    @GetMapping("/history/{senderId}/{receiverId}")
    public List<ChatMessage> getHistory(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return service.getChatHistory(senderId, receiverId);
    }
}
