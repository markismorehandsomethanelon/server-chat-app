package org.example.services.impl;

import org.example.dtos.AccountDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.entities.AccountEntity;
import org.example.entities.UserEntity;
import org.example.mappers.AccountMapper;
import org.example.mappers.UserMapper;
import org.example.repositories.AccountRepository;
import org.example.repositories.UserRepository;
import org.example.requests.ChangePasswordRequest;
import org.example.services.AccountService;
import org.example.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = Logger.getLogger(AccountServiceImpl.class.getName());

    @Value("${default.user.avatar.code}")
    private String defaultUserAvatarCode;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;


    @Override
    public ResponseDTO<UserDTO> signIn(AccountDTO account) {
        Optional<AccountEntity> accountWrapper = accountRepo.findByUsernameAndPassword(
                account.getUsername(), account.getPassword());
        if (!accountWrapper.isPresent()) {
            return ResponseDTO.<UserDTO>builder()
                    .success(false)
                    .message("Username or password is incorrect")
                    .build();
        }

        UserEntity userEntity = accountWrapper.get().getUser();

        return ResponseDTO.<UserDTO>builder()
                .success(true)
                .data(userMapper.convertToDTO(userEntity))
                .build();
    }

    @Override
    public ResponseDTO<UserDTO> signUp(AccountDTO accountDTO) {
        Optional<AccountEntity> accountWrapper = accountRepo.findById(accountDTO.getUsername());
        if (accountWrapper.isPresent()) {
            return ResponseDTO.<UserDTO>builder()
                    .success(false)
                    .message("User is already exists")
                    .build();
        }

        try {

            UserEntity user = UserEntity.builder()
                    .name(accountDTO.getUsername())
                    .avatarCode(this.defaultUserAvatarCode)
                    .build();

            user = userRepo.save(user);

            AccountEntity account = accountMapper.convertToEntity(accountDTO);
            account.setUser(user);

            accountRepo.save(account);


            return ResponseDTO.<UserDTO>builder()
                    .success(true)
                    .data(userMapper.convertToDTO(user))
                    .build();

        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "ERRORRRRRRRRRRRRRRRRRRRRRRRRRR", e);
            return ResponseDTO.<UserDTO>builder()
                    .success(false)
                    .message("Sign up failed")
                    .build();
        }
    }

    @Override
    public ResponseDTO<Void> changePassword(ChangePasswordRequest request) {

        Optional<UserEntity> userWrapper = userRepo.findById(request.getUserId());
        if (!userWrapper.isPresent()){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("User does not exit")
                    .build();
        }

        Optional<AccountEntity> accountWrapper = accountRepo.findByUser(userWrapper.get());

        if (!accountWrapper.isPresent()){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("Account does not exit")
                    .build();
        }

        accountWrapper = accountRepo.findByUsernameAndPassword(
                accountWrapper.get().getUsername(),
                request.getOldPassword());

        if (!accountWrapper.isPresent()) {
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("Old password is incorrect")
                    .build();
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())){
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("New passwords are not the same")
                    .build();
        }

        try
        {
            AccountEntity oldAccount = accountWrapper.get();
            oldAccount.setPassword(request.getNewPassword());

            accountRepo.save(oldAccount);

            return ResponseDTO.<Void>builder()
                    .success(true)
                    .build();

        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "ERRORRRRRRRRRRRRRRRRRRRRRRRRRR", e);
            return ResponseDTO.<Void>builder()
                    .success(false)
                    .message("Change failed")
                    .build();
        }
    }
}
