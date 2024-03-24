package org.example.dtos;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class ContactDTO {
    private Long id;
    private UserDTO sender;
    private UserDTO receiver;
    private String status;
    private Long conversationId;
}
