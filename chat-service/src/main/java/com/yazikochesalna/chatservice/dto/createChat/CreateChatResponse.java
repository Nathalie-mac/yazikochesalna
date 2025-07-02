package com.yazikochesalna.chatservice.dto.createChat;

import lombok.Builder;

@Builder
public record CreateChatResponse(
        long chatId
) {

}
