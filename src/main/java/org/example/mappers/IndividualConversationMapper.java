package org.example.mappers;

import org.example.dtos.*;
import org.example.dtos.IndividualConversationDTO;
import org.example.entities.IndividualConversationEntity;
import org.example.entities.MessageEntity;
import org.example.entities.MultimediaMessageEntity;
import org.example.entities.TextMessageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class IndividualConversationMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private  UserMapper userMapper;

    @Autowired
    private TextMessageMapper textMessageMapper;

    @Autowired
    private MultiMediaMessageMapper multiMediaMessageMapper;
    
    public IndividualConversationEntity convertToEntity(IndividualConversationDTO individualConversationDTO) {

        IndividualConversationEntity individualConversationEntity = modelMapper.map(individualConversationDTO, IndividualConversationEntity.class);

        if (individualConversationDTO.getLastestMessage() != null) {
            MessageDTO lastestMessage = individualConversationDTO.getLastestMessage();
            if (lastestMessage instanceof TextMessageDTO){
                individualConversationEntity.setLastestMessage(textMessageMapper.convertToEntity((TextMessageDTO) lastestMessage));
            }else {
                individualConversationEntity.setLastestMessage(multiMediaMessageMapper.convertToEntity((MultimediaMessageDTO) lastestMessage));
            }
        }

        if (individualConversationDTO.getMessages() != null) {
            individualConversationEntity.setMessages(individualConversationDTO.getMessages().stream()
                    .map(message -> {
                        if (message instanceof TextMessageDTO){
                            return textMessageMapper.convertToEntity((TextMessageDTO) message);
                        }else {
                            return multiMediaMessageMapper.convertToEntity((MultimediaMessageDTO) message);
                        }
                    })
                    .collect(Collectors.toList()));
        }

        if (individualConversationDTO.getMembers() != null) {
            individualConversationEntity.setMembers(individualConversationDTO.getMembers().stream()
                    .map(userMapper::convertToEntity)
                    .collect(Collectors.toList()));
        }

        return individualConversationEntity;
    }

    public IndividualConversationDTO convertToDTO(IndividualConversationEntity individualConversationEntity) {

        IndividualConversationDTO individualConversationDTO = modelMapper.map(individualConversationEntity, IndividualConversationDTO.class);

        if (individualConversationEntity.getLastestMessage() != null) {
            MessageEntity messageEntity = individualConversationEntity.getLastestMessage();
            if (messageEntity instanceof TextMessageEntity) {
                individualConversationDTO.setLastestMessage(textMessageMapper.convertToDTO((TextMessageEntity) messageEntity));
            }else {
                individualConversationDTO.setLastestMessage(multiMediaMessageMapper.convertToDTO((MultimediaMessageEntity) messageEntity));
            }
        }

        if (individualConversationEntity.getMessages() != null) {
            individualConversationDTO.setMessages(individualConversationEntity.getMessages().stream()
                    .map(message -> {
                        if (message instanceof TextMessageEntity){
                            return textMessageMapper.convertToDTO((TextMessageEntity) message);
                        }else {
                            return multiMediaMessageMapper.convertToDTO((MultimediaMessageEntity) message);
                        }
                    })
                    .collect(Collectors.toList()));
        }

        if (individualConversationEntity.getMembers() != null) {
            individualConversationDTO.setMembers(individualConversationEntity.getMembers().stream()
                    .map(userMapper::convertToDTO)
                    .collect(Collectors.toList()));
        }

        individualConversationDTO.setInstanceOf("INDIVIDUAL");

        return individualConversationDTO;
    }
}
