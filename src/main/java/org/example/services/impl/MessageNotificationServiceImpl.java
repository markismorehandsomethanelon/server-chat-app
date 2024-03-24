package org.example.services.impl;


import org.example.dtos.ResponseDTO;
import org.example.entities.MessageNotificationEntity;
import org.example.repositories.MessageNotificationRepository;
import org.example.services.MessageNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageNotificationServiceImpl implements MessageNotificationService {

    @Autowired
    private MessageNotificationRepository messageNotificationRepo;

    @Override
    public ResponseDTO<Void> markAsRead(Long messageNotificationId) {
        Optional<MessageNotificationEntity> messageNotification = messageNotificationRepo.findById(messageNotificationId);
        if (!messageNotification.isPresent()) {
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("Message notification not found").build();
        }

        MessageNotificationEntity messageNotificationEntity = messageNotification.get();
        messageNotificationEntity.setRead(true);
        messageNotificationRepo.save(messageNotificationEntity);

        return ResponseDTO.<Void>builder()
                .success(true)
                .build();
    }
}
