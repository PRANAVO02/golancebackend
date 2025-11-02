package com.golance.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golance.backend.model.Message;
import com.golance.backend.model.User;
import com.golance.backend.repository.MessageRepository;
import com.golance.backend.repository.UserRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public Message sendMessage(Long senderId, Long receiverId, String content) {

        // 1️⃣ Fetch sender and receiver from DB
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // 2️⃣ Build message entity
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setReadStatus(false);

        // 3️⃣ Save and return the new message
        return messageRepository.save(message);
    }

    public List<Message> getConversation(User user1, User user2) {
        return messageRepository.getConversation(user1.getId(), user2.getId());
    }

    public List<User> getChatContacts(Long userId) {
        return messageRepository.findChatContacts(userId);

    }

    public boolean existsBetweenUsers(User u1, User u2) {
        return messageRepository.existsBySenderAndReceiverOrReceiverAndSender(u1, u2, u1, u2);
    }

    public void createEmptyConversation(User sender, User receiver) {
        Message placeholder = new Message();
        placeholder.setSender(sender);
        placeholder.setReceiver(receiver);
        placeholder.setContent(""); // or "[Chat Started]"
        placeholder.setReadStatus(true);
        messageRepository.save(placeholder);
    }


}