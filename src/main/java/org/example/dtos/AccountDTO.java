package org.example.dtos;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Builder
public class AccountDTO {
    private String username;
    private String password;
    private Long userId;
}
