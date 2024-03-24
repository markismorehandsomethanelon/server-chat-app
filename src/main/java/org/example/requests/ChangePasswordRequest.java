package org.example.requests;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class ChangePasswordRequest {
    private Long userId;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
