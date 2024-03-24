package org.example.requests;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Builder
public class LeaveGroupConversationRequest {
    private Long conversationId;
    private Long leaverId;
}
