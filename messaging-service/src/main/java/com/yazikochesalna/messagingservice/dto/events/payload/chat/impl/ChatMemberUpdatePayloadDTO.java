package com.yazikochesalna.messagingservice.dto.events.payload.chat.impl;

import com.yazikochesalna.messagingservice.dto.events.payload.chat.ChatPayloadDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ChatMemberUpdatePayloadDTO extends ChatPayloadDTO {
    @NotNull(message = "memberId не может быть null")
    private Long memberId;

}
