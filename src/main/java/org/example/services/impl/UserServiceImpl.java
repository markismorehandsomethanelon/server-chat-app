package org.example.services.impl;

import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.entities.UserEntity;
import org.example.mappers.UserMapper;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.example.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileUtil fileUtil;

    @Value("${default.user.avatar.code}")
    private String defaultUserAvatarCode;

    @Override
    public ResponseDTO<UserDTO> update(UserDTO userDTO) {
        Optional<UserEntity> userWrapper = userRepo.findById(userDTO.getId());

        if (!userWrapper.isPresent()){
            return ResponseDTO.<UserDTO>builder()
                    .success(false)
                    .message("User does not exist")
                    .build();
        }

        try {
            UserEntity foundUser = userWrapper.get();

            if (!foundUser.getAvatarCode().equals(this.defaultUserAvatarCode)){
                fileUtil.deleteFile(foundUser.getAvatarCode());
            }

            String fileCode = fileUtil.storeFile(userDTO.getAvatarFile());
            foundUser.setAvatarCode(fileCode);

            foundUser.setName(userDTO.getName());

            foundUser = userRepo.save(foundUser);

            return ResponseDTO.<UserDTO>builder()
                    .success(true)
                    .data(userMapper.convertToDTO(foundUser))
                    .build();
        }catch (DataAccessException e){
            return ResponseDTO.<UserDTO>builder()
                    .success(false)
                    .message("Error")
                    .build();
        }
    }

    @Override
    public ResponseDTO<UserDTO> findById(Long id) {
        Optional<UserEntity> userWrapper = userRepo.findById(id);

        if (!userWrapper.isPresent()){
            return ResponseDTO.<UserDTO>builder()
                    .success(false)
                    .message("User does not exist")
                    .build();
        }

        UserDTO userDTO = userMapper.convertToDTO(userWrapper.get());
        userDTO.setAvatarFile(fileUtil.getFile(userWrapper.get().getAvatarCode()));

        return ResponseDTO.<UserDTO>builder()
                .success(true)
                .data(userDTO)
                .build();
    }
}
