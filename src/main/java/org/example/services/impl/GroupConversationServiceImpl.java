package org.example.services.impl;

import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.GroupConversationEntity;
import org.example.entities.UserEntity;
import org.example.mappers.GroupConversationMapper;
import org.example.repositories.GroupConversationRepository;
import org.example.repositories.UserRepository;
import org.example.requests.JoinGroupConversationRequest;
import org.example.requests.LeaveGroupConversationRequest;
import org.example.services.GroupConversationService;
import org.example.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupConversationServiceImpl implements GroupConversationService {

    @Autowired
    private GroupConversationRepository groupConversationRepo;

    @Autowired
    private GroupConversationMapper groupConversationMapper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private FileUtil fileUtil;

    @Override
    public ResponseDTO<GroupConversationDTO> create(GroupConversationDTO groupConversationDTO) {
        try {

            String avatarCode = fileUtil.storeFile(groupConversationDTO.getAvatarFile());

            GroupConversationEntity groupConversationEntity = groupConversationMapper.convertToEntity(groupConversationDTO);
            groupConversationEntity.setAvatarCode(avatarCode);

            groupConversationEntity = groupConversationRepo.save(groupConversationEntity);

            return ResponseDTO.<GroupConversationDTO>builder()
                    .success(true)
                    .data(groupConversationMapper.convertToDTO(groupConversationEntity))
                    .build();
        } catch (Exception e) {
            return ResponseDTO.<GroupConversationDTO>builder()
                    .success(false)
                    .message("Cannot create conversation")
                    .build();
        }
    }

    @Override
    public ResponseDTO<GroupConversationDTO> update(GroupConversationDTO groupConversationDTO) {

        Optional<GroupConversationEntity> groupConversationWrapper = groupConversationRepo.findById(groupConversationDTO.getId());

        if (!groupConversationWrapper.isPresent()){
            return ResponseDTO.<GroupConversationDTO>builder()
                    .success(false)
                    .message("Cannot find conversation")
                    .build();
        }

        GroupConversationEntity foundGroupConversationEntity = groupConversationWrapper.get();

        try {

            fileUtil.deleteFile(foundGroupConversationEntity.getAvatarCode());
            String avatarCode = fileUtil.storeFile(groupConversationDTO.getAvatarFile());

            foundGroupConversationEntity.setName(groupConversationDTO.getName());
            foundGroupConversationEntity.setAvatarCode(avatarCode);

            foundGroupConversationEntity = groupConversationRepo.save(foundGroupConversationEntity);

            return ResponseDTO.<GroupConversationDTO>builder()
                    .success(true)
                    .data(groupConversationMapper.convertToDTO(foundGroupConversationEntity))
                    .build();
        } catch (Exception e) {
            return ResponseDTO.<GroupConversationDTO>builder()
                    .success(false)
                    .message("Cannot create conversation")
                    .build();
        }
    }

    @Override
    public ResponseDTO<GroupConversationDTO> joinConversation(JoinGroupConversationRequest request) {
        Long conversationId;

        try {
            conversationId = Long.valueOf(request.getConversationLink().split("/")[1]);

        } catch (Exception e) {
            return ResponseDTO.<GroupConversationDTO>builder()
                    .success(false)
                    .message("Conversation does not exist")
                    .build();
        }

        Optional<GroupConversationEntity> conversationWrapper = groupConversationRepo.findById(conversationId);

        if (!conversationWrapper.isPresent()) {
            return ResponseDTO.<GroupConversationDTO>builder()
                    .success(false)
                    .message("Cannot find conversation")
                    .build();
        }

        Optional<UserEntity> userWrapper = userRepo.findById(request.getJoinerId());

        if (!userWrapper.isPresent()) {
            return ResponseDTO.<GroupConversationDTO>builder()
                    .success(false)
                    .message("Cannot find user")
                    .build();
        }

        if (groupConversationRepo.existsByIdAndMembersContaining(conversationId, userWrapper.get())) {
            return ResponseDTO.<GroupConversationDTO>builder()
                    .success(false)
                    .message("Conversation already exists")
                    .build();
        }

        try {
            GroupConversationEntity groupConversationEntity = conversationWrapper.get();
            groupConversationEntity.getMembers().add(userWrapper.get());

            groupConversationEntity = groupConversationRepo.save(groupConversationEntity);

            return ResponseDTO.<GroupConversationDTO>builder()
                    .success(true)
                    .data(groupConversationMapper.convertToDTO(groupConversationEntity))
                    .build();

        } catch (Exception e) {
            return ResponseDTO.<GroupConversationDTO>builder()
                    .success(false)
                    .message("Error")
                    .build();
        }
    }

    @Override
    public ResponseDTO<Void> leaveConversation(LeaveGroupConversationRequest request) {
        Optional<GroupConversationEntity> conversationWrapper = groupConversationRepo.findById(request.getConversationId());

        if (!conversationWrapper.isPresent()) {
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("Conversation does not exist")
                    .build();
        }

        Optional<UserEntity> userWrapper = userRepo.findById(request.getLeaverId());

        if (!userWrapper.isPresent()) {
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("User does not exist")
                    .build();
        }

        if (!groupConversationRepo.existsByIdAndMembersContaining(request.getConversationId(), userWrapper.get())) {
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("You is not a member of this group")
                    .build();
        }

        try {
             GroupConversationEntity groupConversationEntity = conversationWrapper.get();

            if (groupConversationEntity.getOwnedBy().equals(userWrapper.get())) {

                groupConversationEntity.getMembers().forEach(member -> member.getConversations().remove(groupConversationEntity));

                groupConversationRepo.delete(groupConversationEntity);

                return ResponseDTO.<Void>builder()
                        .success(true)
                        .build();
            }

            groupConversationEntity.getMembers().remove(userWrapper.get());

            groupConversationRepo.save(groupConversationEntity);

            return ResponseDTO.<Void>builder()
                    .success(true)
                    .build();

        } catch (Exception e) {
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("Error")
                    .build();
        }
    }
}
