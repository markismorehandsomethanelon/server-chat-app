package org.example.services;

import org.example.dtos.MultimediaMessageDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.TextMessageDTO;

public interface MultimediaMessageService {
    ResponseDTO<MultimediaMessageDTO> create(Long conversationId, MultimediaMessageDTO multimediaMessageDTO);
}
