package com.yazikochesalna.chatservice.dto.messagestorageservice;

import lombok.Builder;

@Builder
public record LastMessageInChatDto(
        Long chatId,
        MessageDto message
) {
}
