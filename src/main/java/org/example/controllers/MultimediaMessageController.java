package org.example.controllers;

import org.example.dtos.MultimediaMessageDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.TextMessageDTO;
import org.example.services.MultimediaMessageService;
import org.example.services.TextMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class MultimediaMessageController {

    @Autowired
    private MultimediaMessageService multimediaMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/conversations/{conversationId}/multimediaMessages")
    private void sendTextMessage(@DestinationVariable Long conversationId, MultimediaMessageDTO multimediaMessageDTO) {
        String destination = String.format("/public/conversations/%s/messages", conversationId);
        ResponseDTO<MultimediaMessageDTO> res = multimediaMessageService.create(conversationId, multimediaMessageDTO);
        messagingTemplate.convertAndSend(destination, res);
    }
}
