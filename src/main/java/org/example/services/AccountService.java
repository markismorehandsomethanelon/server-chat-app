package org.example.services;

import org.example.dtos.AccountDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.requests.ChangePasswordRequest;

public interface AccountService {
    ResponseDTO<UserDTO> signIn(AccountDTO account);
    ResponseDTO<UserDTO> signUp(AccountDTO account);
    ResponseDTO<Void> changePassword(ChangePasswordRequest dto);
}
