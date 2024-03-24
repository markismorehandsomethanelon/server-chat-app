package org.example.services;

import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.UserEntity;
import org.example.requests.JoinGroupConversationRequest;
import org.example.requests.LeaveGroupConversationRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ConversationService {
    ResponseDTO<List<ConversationDTO>> findByMemberId(Long memberId);

    ResponseDTO<ConversationDTO> findById(Long id);

    ResponseDTO<ConversationDTO> joinGroupConversation(JoinGroupConversationRequest request);

    ResponseDTO<ConversationDTO> leaveGroupConversation(LeaveGroupConversationRequest request);
}
