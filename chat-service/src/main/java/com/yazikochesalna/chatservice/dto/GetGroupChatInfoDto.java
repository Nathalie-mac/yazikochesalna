package com.yazikochesalna.chatservice.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record GetGroupChatInfoDto(
        @NotNull
        Long chatId,
        @NotNull
        String title,
        @Nullable
        String description,
        @NotNull
        Long ownerId,
        @NotNull
        List<Long> membersIds,
        @Nullable
        UUID avatarUuid
) {
}
