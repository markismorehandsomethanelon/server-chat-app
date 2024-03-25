package org.example.mappers;

import org.example.dtos.MessageDTO;
import org.example.dtos.MessageNotificationDTO;
import org.example.dtos.MultimediaMessageDTO;
import org.example.dtos.TextMessageDTO;
import org.example.entities.MessageEntity;
import org.example.entities.MultimediaMessageEntity;
import org.example.entities.TextMessageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TextMessageMapper {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageNotificationMapper messageNotificationMapper;

    public TextMessageEntity convertToEntity(TextMessageDTO textMessageDTO) {
        TextMessageEntity textMessageEntity = modelMapper.map(textMessageDTO, TextMessageEntity.class);

        if (textMessageDTO.getSender() != null) {
            textMessageEntity.setSender(userMapper.convertToEntity(textMessageDTO.getSender()));
        }

        return textMessageEntity;
    }

    public TextMessageDTO convertToDTO(TextMessageEntity textMessageEntity) {

        TextMessageDTO textMessageDTO = modelMapper.map(textMessageEntity, TextMessageDTO.class);

        if (textMessageEntity.getSender() != null) {
            textMessageDTO.setSender(userMapper.convertToDTO(textMessageEntity.getSender()));
        }

        textMessageDTO.setInstanceOf("TEXT");

        return textMessageDTO;
    }
}
