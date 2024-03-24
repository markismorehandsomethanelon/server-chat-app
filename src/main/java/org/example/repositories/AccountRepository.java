package org.example.repositories;

import org.example.entities.AccountEntity;
import org.example.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {


    Optional<AccountEntity> findByUsernameAndPassword(String username, String password);

    Optional<AccountEntity> findByUser(UserEntity user);

    Optional<AccountEntity> findByUsername(String username);
}
