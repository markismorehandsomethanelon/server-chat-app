package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.ResponseDTO;
import org.example.services.GroupConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class GroupConversationController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupConversationService groupConversationService;

    @PostMapping("/groupConversations")
    public ResponseEntity<ResponseDTO<GroupConversationDTO>> create(
            @RequestBody  GroupConversationDTO groupConversationDTO) {

        ResponseDTO<GroupConversationDTO> res = groupConversationService.create(groupConversationDTO);

        if (res.isSuccess()){
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.badRequest().body(res);
    }

    @PutMapping("/groupConversations")
    public ResponseEntity<ResponseDTO<GroupConversationDTO>> update(
            @RequestBody  GroupConversationDTO groupConversationDTO) {

        ResponseDTO<GroupConversationDTO> res = groupConversationService.update(groupConversationDTO);

        if (res.isSuccess()){
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.badRequest().body(res);
    }
}
