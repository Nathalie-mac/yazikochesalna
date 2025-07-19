package com.yazikochesalna.chatservice.dto.messagestorageservice;

import lombok.Builder;

@Builder
public record PayloadDto(
        Long senderId,
        Long chatId,
        String text
) {
}
