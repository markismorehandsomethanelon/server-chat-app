package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.example.dtos.UserDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String avatarCode;

    @ManyToMany(mappedBy = "members", cascade = {CascadeType.REMOVE})
    private List<ConversationEntity> conversations;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<MessageNotificationEntity> messageNotifications;
}
