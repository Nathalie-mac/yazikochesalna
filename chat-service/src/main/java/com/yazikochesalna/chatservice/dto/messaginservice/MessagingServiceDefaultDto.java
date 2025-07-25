package com.yazikochesalna.chatservice.dto.messaginservice;

import com.yazikochesalna.chatservice.dto.messaginservice.payload.Payload;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MessagingServiceDefaultDto(
        @NotNull
        NotificationType type,
        @NotNull
        Payload payload
) {
}
