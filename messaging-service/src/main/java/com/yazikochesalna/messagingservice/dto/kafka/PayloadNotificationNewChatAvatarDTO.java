package com.yazikochesalna.messagingservice.dto.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadNotificationNewChatAvatarDTO extends PayloadDTO {
    @NotNull(message = "avatarId не может быть null")
    private UUID avatarId;
    @NotNull(message = "memberId не может быть null")
    private Long memberId;

}
