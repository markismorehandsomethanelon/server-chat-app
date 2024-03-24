package org.example.services;

import org.example.dtos.ContactDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.entities.ContactEntity;
import org.example.entities.UserEntity;

import java.util.List;

public interface ContactService {

    ResponseDTO<List<ContactDTO>> findContacts(Long userId);

    ResponseDTO<List<ContactDTO>> findIncomingContactRequests(Long userId);

    ResponseDTO<List<ContactDTO>> findOutgoingContactRequests(Long userId);

    ResponseDTO<ContactDTO> sendOutgoingContactRequest(ContactDTO contactDTO);

    ResponseDTO<ContactDTO> acceptIncomingContactRequest(ContactDTO contactDTO);

    ResponseDTO<Void> declineIncomingContactRequest(ContactDTO contactDTO);

    ResponseDTO<Void> deleteOutgoingContactRequest(ContactDTO contactDTO);

    ResponseDTO<Void> deleteContact(ContactDTO contactDTO);
}
