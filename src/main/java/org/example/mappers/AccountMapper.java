package org.example.mappers;

import org.example.dtos.AccountDTO;
import org.example.dtos.MessageDTO;
import org.example.entities.AccountEntity;
import org.example.entities.MessageEntity;
import org.example.entities.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    @Autowired
    private ModelMapper modelMapper;

    public AccountEntity convertToEntity(AccountDTO accountDTO) {
        AccountEntity accountEntity = modelMapper.map(accountDTO, AccountEntity.class);
        if (accountDTO.getUserId() != null) {
            UserEntity user = new UserEntity();
            user.setId(accountDTO.getUserId());
            accountEntity.setUser(user);
        }
        return accountEntity;
    }

    public AccountDTO convertToDTO(AccountEntity accountEntity) {
        AccountDTO accountDTO = modelMapper.map(accountEntity, AccountDTO.class);
        if (accountEntity.getUser() != null) {
            accountDTO.setUserId(accountEntity.getUser().getId());
        }
        return accountDTO;
    }

}
