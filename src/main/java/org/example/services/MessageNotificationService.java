package org.example.services;

import org.example.dtos.ResponseDTO;

public interface MessageNotificationService {
    ResponseDTO<Void> markAsRead(Long messageNotificationId);
}
