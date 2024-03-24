package org.example.controllers;

import org.example.dtos.ContactDTO;
import org.example.dtos.ResponseDTO;
import org.example.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts/{userId}")
    public ResponseEntity<ResponseDTO<List<ContactDTO>>> findContact(@PathVariable Long userId) {

        ResponseDTO<List<ContactDTO>> res = contactService.findContacts(userId);

        if (res.isSuccess()){
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.badRequest().body(res);
    }

    @GetMapping("/incomingContactRequests/{userId}")
    public ResponseEntity<ResponseDTO<List<ContactDTO>>> findIncomingContactRequests(@PathVariable Long userId) {
        ResponseDTO<List<ContactDTO>> res = contactService.findIncomingContactRequests(userId);

        if (res.isSuccess()){
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.badRequest().body(res);
    }

    @GetMapping("/outgoingContactRequests/{userId}")
    public ResponseEntity<ResponseDTO<List<ContactDTO>>> findOutgoingContactRequests(@PathVariable Long userId) {
        ResponseDTO<List<ContactDTO>> res = contactService.findOutgoingContactRequests(userId);

        if (res.isSuccess()){
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.badRequest().body(res);
    }

    @PostMapping("/outgoingContactRequests")
    public ResponseEntity<ResponseDTO<ContactDTO>> sendOutgoingContactRequest(@RequestBody ContactDTO contactDTO) {

        ResponseDTO<ContactDTO> res = contactService.sendOutgoingContactRequest(contactDTO);

        if (res.isSuccess()) {
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.badRequest().body(res);
    }

    @PutMapping("/incomingContactRequests")
    public ResponseEntity<ResponseDTO<ContactDTO>> acceptIncomingContactRequest(@RequestBody ContactDTO contactDTO) {

        ResponseDTO<ContactDTO> res = contactService.acceptIncomingContactRequest(contactDTO);

        if (res.isSuccess()) {
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.badRequest().body(res);
    }

    @DeleteMapping("/incomingContactRequests")
    public ResponseEntity<ResponseDTO<Void>> declineIncomingContactRequest(@RequestBody ContactDTO contactDTO) {

        ResponseDTO<Void> res = contactService.declineIncomingContactRequest(contactDTO);

        if (res.isSuccess()) {
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.badRequest().body(res);
    }

    @DeleteMapping("/outgoingContactRequests")
    public ResponseEntity<ResponseDTO<Void>> deleteOutgoingContactRequest(@RequestBody ContactDTO contactDTO) {

        ResponseDTO<Void> res = contactService.deleteOutgoingContactRequest(contactDTO);

        if (res.isSuccess()) {
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.badRequest().body(res);
    }

    @DeleteMapping("/contacts")
    public ResponseEntity<ResponseDTO<Void>> deleteContact(@RequestBody ContactDTO contactDTO) {

        ResponseDTO<Void> res = contactService.deleteContact(contactDTO);

        if (res.isSuccess()) {
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.badRequest().body(res);
    }
}
