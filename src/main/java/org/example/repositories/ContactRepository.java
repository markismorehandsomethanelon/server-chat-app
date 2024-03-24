package org.example.repositories;

import org.example.entities.ContactEntity;
import org.example.entities.ContactStatus;
import org.example.entities.UserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {

    Optional<ContactEntity> findOneBySenderAndReceiverOrSenderAndReceiver(UserEntity sender, UserEntity receiver, UserEntity inverseSender, UserEntity inverseReceiver);

    boolean existsBySenderAndReceiverAndStatus(UserEntity sender, UserEntity receiver, ContactStatus status);

    Optional<ContactEntity> findOneBySenderAndReceiver(UserEntity sender, UserEntity receiver);

    @Query("SELECT c FROM ContactEntity c WHERE (c.sender = :sender OR c.receiver = :receiver) AND c.status = :contactStatus")
    List<ContactEntity> findBySenderOrReceiverAndStatus(UserEntity sender, UserEntity receiver, ContactStatus contactStatus);

    List<ContactEntity> findByReceiverAndStatus(UserEntity receiver, ContactStatus contactStatus);

    List<ContactEntity> findBySenderAndStatus(UserEntity sender, ContactStatus contactStatus);
}
