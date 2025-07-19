package com.yazikochesalna.messagingservice.dto.events.payload.chat.impl;

import com.yazikochesalna.messagingservice.dto.events.payload.chat.ChatPayloadDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ChatAvatarPayloadDTO extends ChatPayloadDTO {
    @NotNull(message = "avatarId не может быть null")
    private UUID avatarId;
    @NotNull(message = "memberId не может быть null")
    private Long memberId;

}
