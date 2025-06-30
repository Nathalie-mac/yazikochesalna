package com.yazikochesalna.chatservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.annotation.Nullable;
import lombok.Builder;
import org.aspectj.weaver.ast.Not;

import java.util.List;

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
        List<Long> membersIds
) {
}
