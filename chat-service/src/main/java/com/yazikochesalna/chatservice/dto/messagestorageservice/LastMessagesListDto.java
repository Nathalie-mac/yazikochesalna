package com.yazikochesalna.chatservice.dto.messagestorageservice;

import lombok.Builder;

import java.util.List;

@Builder
public record LastMessagesListDto(
        List<LastMessageInChatDto> messages
) {
}
