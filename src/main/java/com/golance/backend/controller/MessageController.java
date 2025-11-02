package com.golance.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golance.backend.dto.MessageRequestDto;
import com.golance.backend.dto.MessageResponseDto;
import com.golance.backend.model.Message;
import com.golance.backend.model.User;
import com.golance.backend.repository.UserRepository;
import com.golance.backend.service.MessageService;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")

public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserRepository userRepository;

    //get the chat contacts of a particular userId
    @GetMapping("/contacts/{userId}")
    public List<User> getChatContacts(@PathVariable Long userId) {
        return messageService.getChatContacts(userId);
    }

    @PostMapping("/send")
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestBody MessageRequestDto dto) {

        Message savedMessage = messageService.sendMessage(
                dto.getSenderId(),
                dto.getReceiverId(),
                dto.getContent()
        );

        MessageResponseDto response = new MessageResponseDto();
        response.setId(savedMessage.getId());
        response.setSenderId(savedMessage.getSender().getId());
        response.setReceiverId(savedMessage.getReceiver().getId());
        response.setContent(savedMessage.getContent());
        response.setTimestamp(savedMessage.getTimestamp());
        response.setReadStatus(savedMessage.isReadStatus());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //get all chat contents between two user Id's
    @GetMapping("/conversation/{user1Id}/{user2Id}")
    public List<MessageResponseDto> getConversation(@PathVariable Long user1Id, @PathVariable Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Message> messages = messageService.getConversation(user1, user2);

        return messages.stream().map(msg -> {
            MessageResponseDto dto = new MessageResponseDto();
            dto.setId(msg.getId());
            dto.setSenderId(msg.getSender().getId());
            dto.setReceiverId(msg.getReceiver().getId());
            dto.setContent(msg.getContent());
            dto.setTimestamp(msg.getTimestamp());
            dto.setReadStatus(msg.isReadStatus());
            return dto;
        }).toList();
    }


    @PostMapping("/start")
    public ResponseEntity<User> startChat(@RequestBody MessageRequestDto dto) {
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Check if they already have any messages exchanged
        boolean chatExists = messageService.existsBetweenUsers(sender, receiver);

        // If chat doesn't exist, create a dummy "chat-start" entry (optional)
        if (!chatExists) {
            messageService.createEmptyConversation(sender, receiver);
        }

        // return receiver info so frontend can navigate
        return ResponseEntity.ok(receiver);
    }




}