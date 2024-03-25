package org.example.services.impl;


import org.example.dtos.MessageNotificationDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.MessageNotificationEntity;
import org.example.mappers.MessageNotificationMapper;
import org.example.repositories.MessageNotificationRepository;
import org.example.services.MessageNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageNotificationServiceImpl implements MessageNotificationService {

    @Autowired
    private MessageNotificationRepository messageNotificationRepo;
    @Autowired
    private MessageNotificationMapper messageNotificationMapper;

    @Override
    public ResponseDTO<MessageNotificationDTO> markAsRead(Long userId, Long messageId) {
        Optional<MessageNotificationEntity> messageNotification = messageNotificationRepo.findByMessageIdAndUserId(userId, messageId);

        if (!messageNotification.isPresent()) {
            return ResponseDTO.<MessageNotificationDTO>builder()
                    .success(false)
                    .message("Message notification not found").build();
        }

        MessageNotificationEntity messageNotificationEntity = messageNotification.get();
        messageNotificationEntity.setRead(true);
        messageNotificationRepo.save(messageNotificationEntity);

        return ResponseDTO.<MessageNotificationDTO>builder()
                .success(true)
                .data(messageNotificationMapper.toDTO(messageNotificationEntity))
                .build();
    }

    @Override
    public ResponseDTO<Void> markAllAsRead(Long userId, Long conversationId) {
        List<MessageNotificationEntity> messageNotifications = messageNotificationRepo.findUnreadMessagesByUserIdAndConversationId(userId, conversationId);
        messageNotifications.forEach(messageNotificationEntity -> {
            messageNotificationEntity.setRead(true);
            messageNotificationRepo.save(messageNotificationEntity);
        });

        return ResponseDTO.<Void>builder()
                .success(true)
                .build();
    }

    @Override
    public ResponseDTO<List<MessageNotificationDTO>> findUnreadMessagesByUserIdAndConversationId(Long userId, Long conversationId) {

        List< MessageNotificationEntity> messageNotificationEntities = this.messageNotificationRepo.findUnreadMessagesByUserIdAndConversationId(userId, conversationId);
        List<MessageNotificationDTO> messageNotificationDTOS = messageNotificationEntities.stream()
                .map(messageNotificationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseDTO.<List<MessageNotificationDTO>>builder()
                .success(true)
                .data(messageNotificationDTOS)
                .build();
    }


}
