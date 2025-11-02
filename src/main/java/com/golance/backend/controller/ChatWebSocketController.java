package com.golance.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.golance.backend.dto.MessageRequestDto;
import com.golance.backend.dto.MessageResponseDto;
import com.golance.backend.model.Message;
import com.golance.backend.service.MessageService;

@Controller  //prefix is not set as /app prefix is set in WebSocketConfig
public class ChatWebSocketController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageRequestDto messageRequestDto) {

        Message savedMessage = messageService.sendMessage(
                messageRequestDto.getSenderId(),
                messageRequestDto.getReceiverId(),
                messageRequestDto.getContent()
        );

        MessageResponseDto response = new MessageResponseDto();
        response.setId(savedMessage.getId());
        response.setSenderId(savedMessage.getSender().getId());
        response.setReceiverId(savedMessage.getReceiver().getId());
        response.setContent(savedMessage.getContent());
        response.setTimestamp(savedMessage.getTimestamp());
        response.setReadStatus(savedMessage.isReadStatus());

        messagingTemplate.convertAndSendToUser(  //converts the response dto into json and wraps it as a sompt message and then sends to the broker
                String.valueOf(messageRequestDto.getReceiverId()), // user ID as destination
                "/queue/messages",                   // matches /user/queue/messages on client
                response
        );





    }
}