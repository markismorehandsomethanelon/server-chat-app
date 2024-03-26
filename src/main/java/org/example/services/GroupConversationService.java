package org.example.services;

import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.ResponseDTO;
import org.example.requests.JoinGroupConversationRequest;
import org.example.requests.LeaveGroupConversationRequest;
import org.springframework.web.multipart.MultipartFile;

public interface GroupConversationService {
    ResponseDTO<GroupConversationDTO> create(GroupConversationDTO groupConversationDTO);

    ResponseDTO<GroupConversationDTO> update(GroupConversationDTO groupConversationDTO);

    ResponseDTO<GroupConversationDTO> joinConversation(JoinGroupConversationRequest request);

    ResponseDTO<Void> leaveConversation(LeaveGroupConversationRequest leaveGroupConversationRequest);

}
