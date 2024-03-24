package org.example.repositories;


import org.example.dtos.ResponseDTO;
import org.example.entities.MessageEntity;
import org.example.entities.MessageNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageNotificationRepository extends JpaRepository<MessageNotificationEntity, Long> {

    @Query("SELECT COUNT(m) FROM MessageNotificationEntity m WHERE m.read = false AND m.user.id = :userId AND m.message.conversation.id = :conversationId")
    Long countUnreadMessagesByUserIdAndConversationId(@Param("userId") Long userId, @Param("conversationId") Long conversationId);

}
