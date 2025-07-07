package com.yazikochesalna.chatservice.dto;

import com.yazikochesalna.chatservice.enums.ChatType;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ShortChatInfoResponse (
        @NotNull
        UUID lastReadMessageId,
        @NotNull
        ChatType type,
        @NotNull
        List<Long> members
)
{
}
