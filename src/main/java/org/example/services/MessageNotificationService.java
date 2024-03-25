package org.example.services;

import org.example.dtos.MessageNotificationDTO;
import org.example.dtos.ResponseDTO;

import java.util.List;

public interface MessageNotificationService {
    ResponseDTO<Void> markAsRead(Long userId, Long messageId);
    ResponseDTO<List<MessageNotificationDTO>> findUnreadMessagesByUserIdAndConversationId(Long userId, Long conversationId);
}
