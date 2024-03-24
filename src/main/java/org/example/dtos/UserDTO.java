package org.example.dtos;

import lombok.*;
import org.example.entities.UserEntity;

import java.util.List;
import java.util.Objects;


@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class UserDTO {
    private Long id;

    private String name;

    private FileDataDTO avatarFile;

    private List<Long> conversationIds;
}
