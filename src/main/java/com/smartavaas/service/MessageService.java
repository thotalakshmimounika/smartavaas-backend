package com.smartavaas.service;


import com.smartavaas.model.ChatMessage;
import com.smartavaas.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository repo;

    public ChatMessage saveMessage(ChatMessage msg) {
        return repo.save(msg);
    }

    public List<ChatMessage> getChatHistory(Long senderId, Long receiverId) {
        return repo.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestampAsc(
                senderId, receiverId, receiverId, senderId
        );
    }
}

