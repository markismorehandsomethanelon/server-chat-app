package org.example.services;

import org.example.dtos.GroupConversationDTO;
import org.example.dtos.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface GroupConversationService {
    ResponseDTO<GroupConversationDTO> create(GroupConversationDTO groupConversationDTO);

    ResponseDTO<GroupConversationDTO> update(GroupConversationDTO groupConversationDTO);

}
