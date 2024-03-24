package org.example.services.impl;

import org.example.dtos.MultimediaMessageDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.TextMessageDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.MultimediaMessageEntity;
import org.example.entities.TextMessageEntity;
import org.example.entities.UserEntity;
import org.example.mappers.MultiMediaMessageMapper;
import org.example.mappers.TextMessageMapper;
import org.example.repositories.ConversationRepository;
import org.example.repositories.MessageRepository;
import org.example.repositories.UserRepository;
import org.example.services.MultimediaMessageService;
import org.example.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class MultimediaMessageServiceImpl implements MultimediaMessageService {

    @Autowired
    private ConversationRepository conversationRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MultiMediaMessageMapper multiMediaMessageMapper;

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private FileUtil fileUtil;

    @Override
    public ResponseDTO<MultimediaMessageDTO> create(Long conversationId, MultimediaMessageDTO multimediaMessageDTO) {
        Optional<ConversationEntity> conversationWrapper = conversationRepo.findById(conversationId);

        if (!conversationWrapper.isPresent()) {
            return ResponseDTO.<MultimediaMessageDTO>builder()
                    .success(false)
                    .message("Conversation does not exist")
                    .build();
        }

        Optional<UserEntity> userWrapper = userRepo.findById(multimediaMessageDTO.getSender().getId());

        if (!userWrapper.isPresent()) {
            return ResponseDTO.<MultimediaMessageDTO>builder()
                    .success(false)
                    .message("User does not exist")
                    .build();
        }

        try {
            String fileCode = fileUtil.storeFile(multimediaMessageDTO.getDataFile());

            ConversationEntity conversation = conversationWrapper.get();

            MultimediaMessageEntity multimediaMessageEntity = multiMediaMessageMapper.convertToEntity(multimediaMessageDTO);

            multimediaMessageEntity.setFileCode(fileCode);
            multimediaMessageEntity.setConversation(conversation);
            multimediaMessageEntity.setSender(userWrapper.get());
            multimediaMessageEntity = messageRepo.save(multimediaMessageEntity);

            conversation.getMessages().add(multimediaMessageEntity);
            conversation.setLastestMessage(multimediaMessageEntity);

            conversationRepo.save(conversation);

            return ResponseDTO.<MultimediaMessageDTO>builder()
                    .success(true)
                    .data(multiMediaMessageMapper.convertToDTO(multimediaMessageEntity))
                    .build();
        } catch (DataAccessException e) {
            return ResponseDTO.<MultimediaMessageDTO>builder()
                    .success(false)
                    .message("Cannot save messages")
                    .build();
        }
    }
}
