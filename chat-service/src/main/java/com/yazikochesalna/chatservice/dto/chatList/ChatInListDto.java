package com.yazikochesalna.chatservice.dto.chatList;

import com.yazikochesalna.chatservice.enums.ChatType;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ChatInListDto (
    @NotNull
    Long chatId,
    @NotNull
    @Enumerated(EnumType.STRING)
    ChatType type,
    @Nullable
    String title,
    @Nullable
    Long partnerId,
    @NotNull
    Object lastMessage,
    @Size(min = 0)
    int unreadCount,
    @Nullable
    UUID avatarUuid
) {

}
