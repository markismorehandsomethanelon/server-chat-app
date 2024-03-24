package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.entities.ConversationEntity;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "instanceOf"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GroupConversationDTO.class, name = "GROUP"),
        @JsonSubTypes.Type(value = IndividualConversationDTO.class, name = "INDIVIDUAL")
})
public class ConversationDTO {
    private Long id;
    private MessageDTO lastestMessage;
    private List<MessageDTO> messages;
    private List<UserDTO> members;
    private String instanceOf;
    private Long numberOfUnreadMessages;
}
