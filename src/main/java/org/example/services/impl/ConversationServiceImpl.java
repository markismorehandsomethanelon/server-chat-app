package org.example.services.impl;

import org.example.dtos.ConversationDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.GroupConversationEntity;
import org.example.entities.IndividualConversationEntity;
import org.example.entities.UserEntity;
import org.example.mappers.GroupConversationMapper;
import org.example.mappers.IndividualConversationMapper;
import org.example.repositories.ConversationRepository;
import org.example.repositories.GroupConversationRepository;
import org.example.repositories.MessageNotificationRepository;
import org.example.repositories.UserRepository;
import org.example.requests.JoinGroupConversationRequest;
import org.example.requests.LeaveGroupConversationRequest;
import org.example.services.ConversationService;
import org.example.utils.FileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepo;

    @Autowired
    private GroupConversationRepository groupConversationRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MessageNotificationRepository messageNotificationRepo;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GroupConversationMapper groupConversationMapper;

    @Autowired
    private IndividualConversationMapper individualConversationMapper;

    @Override
    public ResponseDTO<List<ConversationDTO>> findByMemberId(Long memberId) {
        List<ConversationEntity> conversationEntityList = conversationRepo.findByUserId(memberId);

        List<ConversationDTO> conversationDTOList = conversationEntityList.stream()
                .map(conversation -> {
                    ConversationDTO conversationDTO = new ConversationDTO();

                    if (conversation instanceof GroupConversationEntity) {
                        conversationDTO = groupConversationMapper.convertToDTO((GroupConversationEntity) conversation);
                    } else {
                        conversationDTO = individualConversationMapper.convertToDTO((IndividualConversationEntity) conversation);
                    }

                    Long unreadMessagesCount = messageNotificationRepo.countUnreadMessagesByUserIdAndConversationId(memberId, conversation.getId());

                    conversationDTO.setNumberOfUnreadMessages(unreadMessagesCount);

                    return conversationDTO;

                })
                .collect(Collectors.toList());
        return ResponseDTO.<List<ConversationDTO>> builder()
                .success(true)
                .data(conversationDTOList)
                .build();
    }

    @Override
    public ResponseDTO<ConversationDTO> findById(Long id) {
        Optional<ConversationEntity> conversationWrapper = conversationRepo.findById(id);
        if (!conversationWrapper.isPresent()){
            return ResponseDTO.<ConversationDTO>builder()
                    .success(false)
                    .message("Conversation does not exist")
                    .build();
        }

        ConversationEntity conversationEntity = conversationWrapper.get();

        ConversationDTO conversationDTO;

        if (conversationEntity instanceof GroupConversationEntity) {
            conversationDTO = groupConversationMapper.convertToDTO((GroupConversationEntity) conversationEntity);
        } else {
            conversationDTO = individualConversationMapper.convertToDTO((IndividualConversationEntity) conversationEntity);
        }

        return ResponseDTO.<ConversationDTO>builder()
                .success(true)
                .data(conversationDTO)
                .build();
    }
}
