package org.example.services;

import org.example.dtos.MessageNotificationDTO;
import org.example.dtos.ResponseDTO;

import java.util.List;

public interface MessageNotificationService {
    ResponseDTO<MessageNotificationDTO> markAsRead(Long userId, Long messageId);
    ResponseDTO<List<MessageNotificationDTO>> findUnreadMessagesByConversationIdAndUserId(Long conversationId, Long userId);

    ResponseDTO<Void> markAllAsRead(Long userId, Long conversationId);
}
