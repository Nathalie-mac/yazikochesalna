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
public class ChatPinnedMessagePayloadDTO extends ChatPayloadDTO {
    @NotNull(message = "pinMessageId не может быть null")
    private UUID pinMessageId;

    private Long memberId;

}
