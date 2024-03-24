package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.UserDTO;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GroupConversationEntity extends ConversationEntity {

    private String name;

    private String avatarCode;

    @ManyToOne
    @JoinColumn(name = "owned_by")
    private UserEntity ownedBy;
}
