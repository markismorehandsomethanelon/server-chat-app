package org.example.services.impl;

import org.example.dtos.ContactDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.*;
import org.example.mappers.ContactMapper;
import org.example.mappers.IndividualConversationMapper;
import org.example.repositories.ContactRepository;
import org.example.repositories.ConversationRepository;
import org.example.repositories.IndividualConversationRepository;
import org.example.repositories.UserRepository;
import org.example.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Transactional
@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private ConversationRepository conversationRepo;

    @Autowired
    private IndividualConversationRepository individualConversationRepo;

    @Autowired
    private IndividualConversationMapper individualConversationMapper;

    @Override
    public ResponseDTO<List<ContactDTO>> findContacts(Long userId) {

        Optional<UserEntity> userWrapper = userRepository.findById(userId);

        if (!userWrapper.isPresent()){
            return ResponseDTO.<List<ContactDTO>>builder()
                    .success(false)
                    .message("Cannot find user")
                    .build();
        }

        UserEntity userEntity = userWrapper.get();
        List<ContactEntity> contactEntityList = contactRepo.findBySenderOrReceiverAndStatus(userEntity, userEntity, ContactStatus.ACCEPTED);

        List<ContactDTO> contactDTOList = contactEntityList.stream()
                .map(contactEntity -> contactMapper.convertToDTO(contactEntity))
                .collect(Collectors.toList());

        return ResponseDTO.<List<ContactDTO>> builder()
                .success(true)
                .data(contactDTOList)
                .build();
    }

    @Override
    public ResponseDTO<List<ContactDTO>> findIncomingContactRequests(Long userId) {

        Optional<UserEntity> userWrapper = userRepository.findById(userId);

        if (!userWrapper.isPresent()){
            return ResponseDTO.<List<ContactDTO>>builder()
                    .success(false)
                    .message("Cannot find user")
                    .build();
        }

        UserEntity userEntity = userWrapper.get();
        List<ContactEntity> contactEntityList = contactRepo.findByReceiverAndStatus(userEntity, ContactStatus.PENDING);

        List<ContactDTO> contactDTOList = contactEntityList.stream()
                .map(contactEntity -> contactMapper.convertToDTO(contactEntity))
                .collect(Collectors.toList());
        return ResponseDTO.<List<ContactDTO>> builder()
                .success(true)
                .data(contactDTOList)
                .build();
    }

    @Override
    public ResponseDTO<List<ContactDTO>> findOutgoingContactRequests(Long userId) {

        Optional<UserEntity> userWrapper = userRepository.findById(userId);

        if (!userWrapper.isPresent()){
            return ResponseDTO.<List<ContactDTO>>builder()
                    .success(false)
                    .message("Cannot find user")
                    .build();
        }

        UserEntity userEntity = userWrapper.get();
        List<ContactEntity> contactEntityList = contactRepo.findBySenderAndStatus(userEntity, ContactStatus.PENDING);

        List<ContactDTO> contactDTOList = contactEntityList.stream()
                .map(contactEntity -> contactMapper.convertToDTO(contactEntity))
                .collect(Collectors.toList());
        return ResponseDTO.<List<ContactDTO>> builder()
                .success(true)
                .data(contactDTOList)
                .build();
    }

    @Override
    public ResponseDTO<ContactDTO> sendOutgoingContactRequest(ContactDTO contactDTO) {
        Optional<UserEntity> senderWrapper = userRepository.findById(contactDTO.getSender().getId());

        if (!senderWrapper.isPresent()){
            return ResponseDTO.<ContactDTO>builder()
                    .success(false)
                    .message("User with id " + contactDTO.getSender().getId() + " does not exist")
                    .build();
        }

        Optional<UserEntity> receiverWrapper = userRepository.findById(contactDTO.getReceiver().getId());

        if (!receiverWrapper.isPresent()){
            return ResponseDTO.<ContactDTO>builder()
                    .success(false)
                    .message("User with id " + contactDTO.getReceiver().getId() + " does not exist")
                    .build();
        }

        UserEntity sender = senderWrapper.get();
        UserEntity receiver = receiverWrapper.get();

        Optional<ContactEntity> foundContactWrapper = contactRepo.findOneBySenderAndReceiverOrSenderAndReceiver(sender, receiver, receiver, sender);

        // Contact relationship does not exist
        if (!foundContactWrapper.isPresent()) {
            try {

                ContactEntity contactEntity = contactMapper.convertToEntity(contactDTO);
                contactEntity.setStatus(ContactStatus.PENDING);
                contactEntity.setSender(sender);
                contactEntity.setReceiver(receiver);
                contactEntity = contactRepo.save(contactEntity);

                return ResponseDTO.<ContactDTO>builder()
                        .success(true)
                        .data(contactMapper.convertToDTO(contactEntity))
                        .build();

            }catch(Exception e){
                return ResponseDTO.<ContactDTO>builder()
                        .success(false)
                        .message("Cannot send request")
                        .build();
            }

        }

        ContactEntity foundContact = foundContactWrapper.get();
        ContactStatus foundContactStatus = foundContact.getStatus();

        if (foundContactStatus.equals(ContactStatus.ACCEPTED)) {
            return ResponseDTO.<ContactDTO>builder()
                    .success(false)
                    .message("Contact already exist")
                    .build();
        }

        if (foundContact.getSender().equals(sender)) {
            return ResponseDTO.<ContactDTO>builder()
                    .success(false)
                    .message("Request already exist")
                    .build();
        }

        try {

            ConversationEntity conversationEntity = ConversationEntity
                    .builder()
                    .members(Arrays.asList(receiver, sender))
                    .build();

            conversationEntity = conversationRepo.save(conversationEntity);

            foundContact.setStatus(ContactStatus.ACCEPTED);
            foundContact.setConversationEntity(conversationEntity);
            foundContact = contactRepo.save(foundContact);

            return ResponseDTO.<ContactDTO>builder()
                    .success(true)
                    .data(contactMapper.convertToDTO(foundContact))
                    .build();

        }catch(Exception e){
            return ResponseDTO.<ContactDTO>builder()
                    .success(false)
                    .message("Cannot send request")
                    .build();
        }


    }

    @Override
    public ResponseDTO<ContactDTO> acceptIncomingContactRequest(ContactDTO contactDTO) {
        Optional<UserEntity> senderWrapper = userRepository.findById(contactDTO.getSender().getId());

        if (!senderWrapper.isPresent()){
            return ResponseDTO.<ContactDTO>builder()
                    .success(false)
                    .message("User with id " + contactDTO.getSender().getId() + " does not exist")
                    .build();
        }

        Optional<UserEntity> receiverWrapper = userRepository.findById(contactDTO.getReceiver().getId());

        if (!receiverWrapper.isPresent()){
            return ResponseDTO.<ContactDTO>builder()
                    .success(false)
                    .message("User with id " + contactDTO.getReceiver().getId() + " does not exist")
                    .build();
        }

        UserEntity sender = senderWrapper.get();
        UserEntity receiver = receiverWrapper.get();


        try {

            IndividualConversationEntity individualConversationEntity = IndividualConversationEntity
                    .builder()
                    .members(Arrays.asList(sender, receiver))
                    .build();

            individualConversationEntity = individualConversationRepo.save(individualConversationEntity);

            ContactEntity contactEntity = contactMapper.convertToEntity(contactDTO);
            contactEntity.setSender(sender);
            contactEntity.setReceiver(receiver);
            contactEntity.setStatus(ContactStatus.ACCEPTED);
            contactEntity.setConversationEntity(individualConversationEntity);

            contactEntity = contactRepo.save(contactEntity);

            contactDTO = contactMapper.convertToDTO(contactEntity);

            return ResponseDTO.<ContactDTO>builder()
                    .success(true)
                    .data(contactDTO)
                    .build();

        }catch (Exception e){
            return ResponseDTO.<ContactDTO>builder()
                    .success(false)
                    .message("Cannot accept incoming request")
                    .build();
        }
    }

    @Override
    public ResponseDTO<Void> declineIncomingContactRequest(ContactDTO contactDTO) {
        Optional<UserEntity> senderWrapper = userRepository.findById(contactDTO.getSender().getId());

        if (!senderWrapper.isPresent()){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("User with id " + contactDTO.getSender().getId() + " does not exist")
                    .build();
        }

        Optional<UserEntity> receiverWrapper = userRepository.findById(contactDTO.getReceiver().getId());

        if (!receiverWrapper.isPresent()){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("User with id " + contactDTO.getReceiver().getId() + " does not exist")
                    .build();
        }

        try {
            ContactEntity contactEntity = contactMapper.convertToEntity(contactDTO);
            contactEntity.setSender(senderWrapper.get());
            contactEntity.setReceiver(receiverWrapper.get());

            contactRepo.delete(contactEntity);

            return ResponseDTO.<Void>builder()
                    .success(true)
                    .build();

        }catch (Exception e){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("Cannot decline incoming request")
                    .build();
        }
    }

    @Transactional
    @Override
    public ResponseDTO<Void> deleteOutgoingContactRequest(ContactDTO contactDTO) {
        Optional<UserEntity> senderWrapper = userRepository.findById(contactDTO.getSender().getId());

        if (!senderWrapper.isPresent()){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("User with id " + contactDTO.getSender().getId() + " does not exist")
                    .build();
        }

        Optional<UserEntity> receiverWrapper = userRepository.findById(contactDTO.getReceiver().getId());

        if (!receiverWrapper.isPresent()){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("User with id " + contactDTO.getReceiver().getId() + " does not exist")
                    .build();
        }

        Optional<ContactEntity> contactWrapper = contactRepo.findById(contactDTO.getId());

        if (!contactWrapper.isPresent()) {
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("Cannot find contact")
                    .build();
        }

        try {
            ContactEntity contactEntity = contactWrapper.get();

            contactRepo.delete(contactEntity);

            return ResponseDTO.<Void>builder()
                    .success(true)
                    .build();

        }catch (Exception e){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("Cannot delete outgoing request")
                    .build();
        }
    }

    @Override
    public ResponseDTO<Void> deleteContact(ContactDTO contactDTO) {
        Optional<UserEntity> senderWrapper = userRepository.findById(contactDTO.getSender().getId());

        if (!senderWrapper.isPresent()){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("User with id " + contactDTO.getSender().getId() + " does not exist")
                    .build();
        }

        Optional<UserEntity> receiverWrapper = userRepository.findById(contactDTO.getReceiver().getId());

        if (!receiverWrapper.isPresent()){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("User with id " + contactDTO.getReceiver().getId() + " does not exist")
                    .build();
        }

        Optional<ContactEntity> contactWrapper = contactRepo.findById(contactDTO.getId());

        if (!contactWrapper.isPresent()) {
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("Cannot find contact")
                    .build();
        }

        try {
            ContactEntity contactEntity = contactWrapper.get();

            Optional<ConversationEntity> conversationWrapper = conversationRepo.findById(contactEntity.getConversationEntity().getId());

            if (!conversationWrapper.isPresent()){
                return ResponseDTO.<Void>builder()
                        .success(false)
                        .message("Cannot find conversation")
                        .build();
            }

            ConversationEntity conversationEntity = conversationWrapper.get();
            conversationEntity.getMembers().forEach(member -> member.getConversations().remove(conversationEntity));
            conversationRepo.delete(conversationEntity);

            contactRepo.delete(contactEntity);

            return ResponseDTO.<Void>builder()
                    .success(true)
                    .build();

        }catch (Exception e){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("Cannot delete contact")
                    .build();
        }
    }
}
