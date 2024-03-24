package org.example.controllers;
import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.entities.AccountEntity;
import org.example.entities.UserEntity;
import org.example.services.AccountService;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @PutMapping("/users")
    public ResponseEntity<ResponseDTO<UserDTO>> update(@RequestBody UserDTO user) {
        ResponseDTO<UserDTO> res = userService.update(user);
        if (res.isSuccess()){
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.badRequest().body(res);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseDTO<UserDTO>> findById(@PathVariable Long userId) {
        ResponseDTO<UserDTO> res = userService.findById(userId);

        if (res.isSuccess()){
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.badRequest().body(res);
    }

}
