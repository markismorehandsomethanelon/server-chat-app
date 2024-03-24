package org.example.services;

import org.example.dtos.MessageDTO;
import org.example.dtos.MessageNotificationDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.TextMessageDTO;

import java.util.List;

public interface TextMessageService {
    ResponseDTO<TextMessageDTO> create(Long conversationId, TextMessageDTO textMessageDTO);
}
