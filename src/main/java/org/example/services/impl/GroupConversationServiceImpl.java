package org.example.services.impl;

import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.GroupConversationEntity;
import org.example.mappers.GroupConversationMapper;
import org.example.repositories.GroupConversationRepository;
import org.example.services.FileStorageService;
import org.example.services.GroupConversationService;
import org.example.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class GroupConversationServiceImpl implements GroupConversationService {

    @Autowired
    private GroupConversationRepository groupConversationRepo;

    @Autowired
    private GroupConversationMapper groupConversationMapper;

    @Autowired
    private FileUtil fileUtil;

    @Value("${default.group.avatar.code}")
    private String defaultGroupConversationAvatarCode;

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
}
