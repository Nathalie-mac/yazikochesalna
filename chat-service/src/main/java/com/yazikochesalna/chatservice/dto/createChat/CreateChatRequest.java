package com.yazikochesalna.chatservice.dto.createChat;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateChatRequest(
        @NotNull @NotEmpty String title,
        @Nullable String description,
        @Nullable List<Long> membersIds
) {

}
