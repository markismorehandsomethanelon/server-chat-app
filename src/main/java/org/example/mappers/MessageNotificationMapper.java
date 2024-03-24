package org.example.mappers;

import org.example.dtos.MessageDTO;
import org.example.dtos.MessageNotificationDTO;
import org.example.dtos.UserDTO;
import org.example.entities.MessageEntity;
import org.example.entities.MessageNotificationEntity;
import org.example.entities.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageNotificationMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TextMessageMapper textMessageMapper;

    @Autowired
    private MultiMediaMessageMapper multiMediaMessageMapper;

    public MessageNotificationEntity toEntity(MessageNotificationDTO messageNotificationDTO) {
        MessageNotificationEntity messageNotificationEntity = modelMapper.map(messageNotificationDTO, MessageNotificationEntity.class);

        if (messageNotificationDTO.getUserId() != null) {
            messageNotificationEntity.setUser(userMapper.convertToEntity(UserDTO.builder()
                    .id(messageNotificationDTO.getUserId())
                    .build()));
        }

        if (messageNotificationDTO.getMessageId() != null) {
            messageNotificationEntity.setMessage(MessageEntity.builder()
                    .id(messageNotificationDTO.getId())
                    .build());
        }

        return messageNotificationEntity;
    }

    public MessageNotificationDTO toDTO(MessageNotificationEntity messageNotificationEntity) {
        MessageNotificationDTO messageNotificationDTO = modelMapper.map(messageNotificationEntity, MessageNotificationDTO.class);

        if (messageNotificationEntity.getUser() != null) {
            messageNotificationDTO.setUserId(messageNotificationEntity.getUser().getId());
        }

        if (messageNotificationEntity.getMessage() != null) {
            messageNotificationDTO.setMessageId(messageNotificationEntity.getMessage().getId());
        }

        return messageNotificationDTO;
    }
}
