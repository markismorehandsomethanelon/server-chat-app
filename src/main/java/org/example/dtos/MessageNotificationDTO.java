package org.example.dtos;


import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class MessageNotificationDTO {
    private Long id;
    private boolean read;
    private Long userId;
    private Long messageId;
}
