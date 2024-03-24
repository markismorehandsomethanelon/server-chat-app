package org.example.mappers;

import org.example.dtos.*;
import org.example.entities.GroupConversationEntity;
import org.example.entities.MessageEntity;
import org.example.entities.MultimediaMessageEntity;
import org.example.entities.TextMessageEntity;
import org.example.utils.FileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GroupConversationMapper {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TextMessageMapper textMessageMapper;

    @Autowired
    private MultiMediaMessageMapper multiMediaMessageMapper;

    @Autowired
    private FileUtil fileUtil;

    public GroupConversationEntity convertToEntity(GroupConversationDTO groupConversationDTO) {

        GroupConversationEntity groupConversationEntity = modelMapper.map(groupConversationDTO, GroupConversationEntity.class);

        if (groupConversationDTO.getLastestMessage() != null) {
            MessageDTO lastestMessage = groupConversationDTO.getLastestMessage();
            if (lastestMessage instanceof TextMessageDTO){
                groupConversationEntity.setLastestMessage(textMessageMapper.convertToEntity((TextMessageDTO) lastestMessage));
            }else {
                groupConversationEntity.setLastestMessage(multiMediaMessageMapper.convertToEntity((MultimediaMessageDTO) lastestMessage));
            }
        }

        if (groupConversationDTO.getMessages() != null) {
            groupConversationEntity.setMessages(groupConversationDTO.getMessages().stream()
                    .map(message -> {
                        if (message instanceof TextMessageDTO){
                            return textMessageMapper.convertToEntity((TextMessageDTO) message);
                        }else {
                            return multiMediaMessageMapper.convertToEntity((MultimediaMessageDTO) message);
                        }
                    })
                    .collect(Collectors.toList()));
        }

        if (groupConversationDTO.getMembers() != null) {
            groupConversationEntity.setMembers(groupConversationDTO.getMembers().stream()
                    .map(userMapper::convertToEntity)
                    .collect(Collectors.toList()));
        }

        if (groupConversationDTO.getOwnerId() != null) {
            groupConversationEntity.setOwnedBy(userMapper.convertToEntity(UserDTO.builder().id(groupConversationDTO.getOwnerId()).build()));
        }

        return groupConversationEntity;
    }

    public GroupConversationDTO convertToDTO(GroupConversationEntity groupConversationEntity) {

        GroupConversationDTO groupConversationDTO = modelMapper.map(groupConversationEntity, GroupConversationDTO.class);

        if (groupConversationEntity.getLastestMessage() != null) {
            MessageEntity messageEntity = groupConversationEntity.getLastestMessage();
            if (messageEntity instanceof TextMessageEntity) {
                groupConversationDTO.setLastestMessage(textMessageMapper.convertToDTO((TextMessageEntity) messageEntity));
            }else {
                groupConversationDTO.setLastestMessage(multiMediaMessageMapper.convertToDTO((MultimediaMessageEntity) messageEntity));
            }
        }

        if (groupConversationEntity.getMessages() != null) {
            groupConversationDTO.setMessages(groupConversationEntity.getMessages().stream()
                    .map(message -> {
                        if (message instanceof TextMessageEntity){
                            return textMessageMapper.convertToDTO((TextMessageEntity) message);
                        }else {
                            return multiMediaMessageMapper.convertToDTO((MultimediaMessageEntity) message);
                        }
                    })
                    .collect(Collectors.toList()));
        }

        if (groupConversationEntity.getMembers() != null) {
            groupConversationDTO.setMembers(groupConversationEntity.getMembers().stream()
                    .map(userMapper::convertToDTO)
                    .collect(Collectors.toList()));
        }

        if (groupConversationEntity.getOwnedBy() != null) {
            groupConversationDTO.setOwnerId(userMapper.convertToDTO(groupConversationEntity.getOwnedBy()).getId());
        }

        groupConversationDTO.setAvatarFile(fileUtil.getFile(groupConversationEntity.getAvatarCode()));
        groupConversationDTO.setInstanceOf("GROUP");

        return groupConversationDTO;
    }
}


