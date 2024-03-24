package org.example.repositories;

import org.example.entities.ConversationEntity;
import org.example.entities.IndividualConversationEntity;
import org.example.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    @Query("SELECT c FROM ConversationEntity c JOIN c.members m WHERE m.id = :userId")
    List<ConversationEntity> findByUserId(@Param("userId") Long userId);

    boolean existsByIdAndMembersContaining(Long conversationId, UserEntity user);

    @Query("SELECT c FROM ConversationEntity c " +
            "JOIN c.members m" +
            " WHERE m IN :members")
    Optional<ConversationEntity> findFirstByMembers(@Param("members") List<UserEntity> members);

}
