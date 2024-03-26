package org.example.repositories;

import org.example.entities.ConversationEntity;
import org.example.entities.GroupConversationEntity;
import org.example.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupConversationRepository extends JpaRepository<GroupConversationEntity, Long>{
    boolean existsByIdAndMembersContaining(Long conversationId, UserEntity user);
}
