package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.entities.MessageEntity;
import org.example.entities.MessageNotificationEntity;

import java.sql.Timestamp;
import java.util.List;

@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "instanceOf"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextMessageDTO.class, name = "TEXT"),
            @JsonSubTypes.Type(value = MultimediaMessageDTO.class, name = "MULTIMEDIA")
})
public class MessageDTO {
    private Long id;
    private Timestamp sentAt;
    private UserDTO sender;
    private Long conversationId;
    private String instanceOf;
    private List<MessageNotificationDTO> notifications;
}