package org.example.services;

import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.entities.UserEntity;

public interface UserService {
    ResponseDTO<UserDTO> update(UserDTO user);

    ResponseDTO<UserDTO> findById(Long id);
}
