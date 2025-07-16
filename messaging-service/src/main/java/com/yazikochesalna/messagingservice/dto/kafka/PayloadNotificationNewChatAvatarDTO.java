package com.yazikochesalna.messagingservice.dto.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadNotificationNewChatAvatarDTO extends PayloadDTO{
    @NotNull(message = "avatarId не может быть null")
    private UUID avatarId;
    @NotNull(message = "memberId не может быть null")
    private Long memberId;
    @NotNull(message = "chatId не может быть null")
    private Long chatId;
}
