package com.yazikochesalna.chatservice.dto.messaginservice.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class AvatarPayload extends Payload {
    @NotNull
    public UUID avatarId;
    @NotNull
    public Long memberId;
    @NotNull
    public Long chatId;

}
