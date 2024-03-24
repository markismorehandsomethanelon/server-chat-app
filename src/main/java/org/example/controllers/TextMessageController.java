package org.example.controllers;

import org.example.dtos.MessageNotificationDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.TextMessageDTO;
import org.example.services.TextMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1")
public class TextMessageController {
    @Autowired
    private TextMessageService textMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/conversations/{conversationId}/textMessages")
    private void sendTextMessage(@DestinationVariable Long conversationId, TextMessageDTO textMessageDTO) {
        String destination = String.format("/public/conversations/%s/messages", conversationId);

        ResponseDTO<TextMessageDTO> res = textMessageService.create(conversationId, textMessageDTO);

//        if (!res.isSuccess()){
            messagingTemplate.convertAndSend(destination, res);
//            return;
//        }

//        res.getData().forEach(messageNotificationDTO -> {
//            String destination = String.format("/private/user/%s/conversations/%s/messages", messageNotificationDTO.getUser().getId(), conversationId);
//            messagingTemplate.convertAndSend(destination, messageNotificationDTO);
//        });
    }

}
