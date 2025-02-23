package com.TwinStar.TwinStar.chat.controller;

import com.TwinStar.TwinStar.chat.dto.ChatMessageDto;
import com.TwinStar.TwinStar.chat.service.ChatService;
import com.TwinStar.TwinStar.chat.service.RedisPubSubService;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class StompController {

    private final SimpMessageSendingOperations messageTemplate;
    private final ChatService chatService;
    private final RedisPubSubService pubSubService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public StompController(SimpMessageSendingOperations messageTemplate, ChatService chatService, RedisPubSubService pubSubService, UserRepository userRepository, SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messageTemplate = messageTemplate;
        this.chatService = chatService;
        this.pubSubService = pubSubService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable("roomId") Long roomId, ChatMessageDto chatMessageDto) throws JsonProcessingException {
        chatMessageDto.setRoomId(roomId);
        chatMessageDto.setSendTime(LocalDateTime.now());
        chatService.messageSave(chatMessageDto,roomId);

//        messagingTemplate.convertAndSend("/topic/" + roomId, chatMessageDto);

        String message = objectMapper.writeValueAsString(chatMessageDto);
        pubSubService.publish("chat", message);

    }
}
