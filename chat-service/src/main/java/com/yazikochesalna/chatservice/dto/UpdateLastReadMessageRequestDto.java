package com.yazikochesalna.chatservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateLastReadMessageRequestDto(
        @NotNull
        UUID lastRead
) {
}
