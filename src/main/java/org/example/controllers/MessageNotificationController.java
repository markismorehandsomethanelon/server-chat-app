package org.example.controllers;

import org.example.dtos.ResponseDTO;
import org.example.services.MessageNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageNotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageNotificationService messageNotificationService;
    @MessageMapping("/a/{userId}/messageNotifications/{messageNotificationId}/markAsRead")
    public void markAsRead(@DestinationVariable Long userId, @DestinationVariable Long messageNotificationId) {
        String destination = String.format("/private/a/%s/messageNotifications", userId);
        ResponseDTO<Void> res = this.messageNotificationService.markAsRead(messageNotificationId);
        messagingTemplate.convertAndSend(destination, res);
    }
}
