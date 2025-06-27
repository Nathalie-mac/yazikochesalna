package com.yazikochesalna.chatservice.dto.chatList;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record ChatListDto(
    @NotNull
    List<ChatInListDto> chats
) {

}
