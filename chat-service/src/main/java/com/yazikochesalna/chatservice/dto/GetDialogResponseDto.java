package com.yazikochesalna.chatservice.dto;

import jakarta.validation.constraints.NotNull;

public record GetDialogResponseDto(
        @NotNull
        Long chatId
) {
}
