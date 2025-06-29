package com.yazikochesalna.chatservice.dto.chatList;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ChatInListDto (
    @NotNull
    String chatId,
    @NotNull
    String type,
    @NotNull
    String title,
    @Nullable
    Long partnerId,
    @Size(min = 0)
    int unreadCount
) {

}
