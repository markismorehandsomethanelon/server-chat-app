package org.example.controllers;

import org.example.dtos.AccountDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.requests.ChangePasswordRequest;
import org.example.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/signin")
    public ResponseEntity<ResponseDTO<UserDTO>> signIn(@RequestBody AccountDTO account) {
        ResponseDTO<UserDTO> res = accountService.signIn(account);
        if (res.isSuccess()){
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.badRequest().body(res);
    }
    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<UserDTO>> signUp(@RequestBody AccountDTO account) {
        ResponseDTO<UserDTO> res = accountService.signUp(account);
        if (res.isSuccess()){
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.badRequest().body(res);
    }

    @PutMapping("/accounts")
    public ResponseEntity<ResponseDTO<Void>> changePassword(@RequestBody ChangePasswordRequest request){
        ResponseDTO<Void> res = accountService.changePassword(request);
        if (res.isSuccess()){
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.badRequest().body(res);
    }
}
