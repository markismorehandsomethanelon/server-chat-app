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
        String destination = String.format("/private/user/%s/messageNotifications/markAsRead", userId);
        ResponseDTO<MessageNotificationDTO> res = this.messageNotificationService.markAsRead(userId, messageId);
        messagingTemplate.convertAndSend(destination, res);
    }

    @MessageMapping("/user/{userId}/conversation/{conversationId}/messageNotifications/markAllAsRead")
    public void markAllAsRead(@DestinationVariable Long userId, @DestinationVariable Long conversationId) {
        String destination = String.format("/private/user/%s/messageNotifications/markAllAsRead", userId);
        ResponseDTO<Void> res = this.messageNotificationService.markAllAsRead(userId, conversationId);
        messagingTemplate.convertAndSend(destination, res);
    }


    @GetMapping("/conversations/{conversationId}/unreadMessages/user/{userId}")
    public ResponseEntity<ResponseDTO<List<MessageNotificationDTO>>> findUnreadMessagesByUserIdAndConversationId(@DestinationVariable Long conversationId, @DestinationVariable Long userId) {
        return ResponseEntity.ok(this.messageNotificationService.findUnreadMessagesByConversationIdAndUserId(conversationId, userId));
    }
}
