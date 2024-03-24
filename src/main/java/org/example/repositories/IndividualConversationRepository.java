package org.example.repositories;

import org.example.entities.IndividualConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualConversationRepository extends JpaRepository<IndividualConversationEntity, Long> {
}
