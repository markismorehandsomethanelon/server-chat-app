package org.example.controllers;

import org.example.dtos.MessageNotificationDTO;
import org.example.dtos.ResponseDTO;
import org.example.services.MessageNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/messageNotifications")
public class MessageNotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageNotificationService messageNotificationService;
    @MessageMapping("/user/{userId}/message/{messageId}/messageNotifications/markAsRead")
    public void markAsRead(@DestinationVariable Long userId, @DestinationVariable Long messageId) {
        String destination = String.format("/private/user/%s/messageNotifications", userId);
        ResponseDTO<Void> res = this.messageNotificationService.markAsRead(userId, messageId);
        messagingTemplate.convertAndSend(destination, res);
    }


    @GetMapping("/user/{userId}/conversation/{conversationId}")
    public ResponseEntity<ResponseDTO<List<MessageNotificationDTO>>> findUnreadMessagesByUserIdAndConversationId(@DestinationVariable Long userId, @DestinationVariable Long conversationId) {
        return ResponseEntity.ok(this.messageNotificationService.findUnreadMessagesByUserIdAndConversationId(userId, conversationId));
    }
}
