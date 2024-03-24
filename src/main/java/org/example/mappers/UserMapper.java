package org.example.mappers;

import org.example.dtos.UserDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.UserEntity;
import org.example.utils.FileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileUtil fileUtil;

    public UserEntity convertToEntity(UserDTO userDTO) {
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        if (userDTO.getConversationIds() != null) {
            userEntity.setConversations(userDTO.getConversationIds().stream()
                    .map(conversationId -> ConversationEntity.builder().id(conversationId).build())
                    .collect(Collectors.toList()));
        }
        return userEntity;
    }

    public UserDTO convertToDTO(UserEntity userEntity) {
        UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
        if (userEntity.getConversations() != null) {
            userDTO.setConversationIds(userEntity.getConversations().stream()
                    .map(ConversationEntity::getId)
                    .collect(Collectors.toList()));
        }
        userDTO.setAvatarFile(fileUtil.getFile(userEntity.getAvatarCode()));

        return userDTO;
    }
}
