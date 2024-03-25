package org.example.services.impl;
import org.example.dtos.MessageDTO;
import org.example.dtos.MessageNotificationDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.TextMessageDTO;
import org.example.entities.*;
import org.example.mappers.MessageNotificationMapper;
import org.example.mappers.TextMessageMapper;
import org.example.repositories.ConversationRepository;
import org.example.repositories.MessageNotificationRepository;
import org.example.repositories.MessageRepository;
import org.example.repositories.UserRepository;
import org.example.services.TextMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class TextMessageServiceImpl implements TextMessageService {

    @Autowired
    private TextMessageMapper textMessageMapper;

    @Autowired
    private MessageNotificationMapper messageNotificationMapper;

    @Autowired
    private ConversationRepository conversationRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private MessageNotificationRepository messageNotificationRepo;

    @Override
    public ResponseDTO<TextMessageDTO> create(Long conversationId, TextMessageDTO textMessageDTO) {

        Optional<ConversationEntity> conversationWrapper = conversationRepo.findById(conversationId);

        if (!conversationWrapper.isPresent()) {
            return ResponseDTO.<TextMessageDTO>builder()
                    .success(false)
                    .message("Conversation does not exist")
                    .build();
        }

        Optional<UserEntity> userWrapper = userRepo.findById(textMessageDTO.getSender().getId());

        if (!userWrapper.isPresent()) {
            return ResponseDTO.<TextMessageDTO>builder()
                    .success(false)
                    .message("User does not exist")
                    .build();
        }

        try {
            TextMessageEntity textMessageEntity = textMessageMapper.convertToEntity(textMessageDTO);

            ConversationEntity conversation = conversationWrapper.get();
            conversation.getMessages().add(textMessageEntity);
            conversation.setLastestMessage(textMessageEntity);
            conversationRepo.save(conversation);

            textMessageEntity.setConversation(conversation);
            textMessageEntity.setSender(userWrapper.get());

            TextMessageEntity finalTextMessageEntity = textMessageEntity;
//            conversation.getMembers().forEach(member -> {
//                MessageNotificationEntity messageNotificationEntity = MessageNotificationEntity.builder()
//                        .message(finalTextMessageEntity)
//                        .user(member)
//                        .read(false)
//                        .build();
//
//                finalTextMessageEntity.getNotifications().add(messageNotificationEntity);
//            });

            textMessageEntity = messageRepo.save(textMessageEntity);

            return ResponseDTO.<TextMessageDTO>builder()
                    .success(true)
                    .data(textMessageMapper.convertToDTO(textMessageEntity))
                    .build();
        } catch (DataAccessException e) {
            return ResponseDTO.<TextMessageDTO>builder()
                    .success(false)
                    .message("Cannot save messages")
                    .build();
        }
    }
}
