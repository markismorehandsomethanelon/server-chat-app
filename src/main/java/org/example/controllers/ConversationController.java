package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.UserEntity;
import org.example.requests.JoinGroupConversationRequest;
import org.example.requests.LeaveGroupConversationRequest;
import org.example.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/conversations/members/{memberId}")
    public ResponseEntity<ResponseDTO<List<ConversationDTO>>> findByMember(
            @PathVariable Long memberId) {

        return ResponseEntity.ok().body(conversationService.findByMemberId(memberId));
    }

    @GetMapping("/conversations/{id}")
    public ResponseEntity<ResponseDTO<ConversationDTO>> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok().body(conversationService.findById(id));
    }



}
