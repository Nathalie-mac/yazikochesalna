package com.yazikochesalna.chatservice.dto.messaginservice;

import jakarta.validation.constraints.NotNull;

public record MessagingServiceNotificationDto(
        @NotNull
        Type type,
        @NotNull
        Payload payload
) {

    public static record Payload(
            @NotNull
            Long chatId,
            @NotNull
            Long memberId
    ) {
    }

    public static enum Type {
        NEW_MEMBER,
        DROP_MEMBER,
    }
}
