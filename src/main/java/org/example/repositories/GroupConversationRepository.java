package org.example.repositories;

import org.example.entities.ConversationEntity;
import org.example.entities.GroupConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupConversationRepository extends JpaRepository<GroupConversationEntity, Long>{

}
