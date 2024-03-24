package org.example.mappers;

import org.example.dtos.ContactDTO;
import org.example.entities.AccountEntity;
import org.example.entities.ContactEntity;
import org.example.entities.ConversationEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMapper userMapper;

    public ContactEntity convertToEntity(ContactDTO contactDTO){
        ContactEntity contactEntity = modelMapper.map(contactDTO, ContactEntity.class);
        if (contactDTO.getSender() != null) {
            contactEntity.setSender(userMapper.convertToEntity(contactDTO.getSender()));
        }
        if (contactDTO.getReceiver() != null) {
            contactEntity.setReceiver(userMapper.convertToEntity(contactDTO.getReceiver()));
        }
        if (contactDTO.getConversationId() != null){
            contactEntity.setConversationEntity(ConversationEntity.builder().id(contactDTO.getConversationId()).build());

        }
        return contactEntity;
    }

    public ContactDTO convertToDTO(ContactEntity contactEntity){
        ContactDTO contactDTO = modelMapper.map(contactEntity, ContactDTO.class);
        if (contactEntity.getSender() != null) {
            contactDTO.setSender(userMapper.convertToDTO(contactEntity.getSender()));
        }
        if (contactEntity.getReceiver() != null) {
            contactDTO.setReceiver(userMapper.convertToDTO(contactEntity.getReceiver()));
        }
        if (contactEntity.getConversationEntity() != null){
            contactDTO.setConversationId(contactEntity.getConversationEntity().getId());
        }
        return contactDTO;
    }
}
