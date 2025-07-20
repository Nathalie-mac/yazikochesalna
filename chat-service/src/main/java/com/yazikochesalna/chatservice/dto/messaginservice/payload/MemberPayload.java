package com.yazikochesalna.chatservice.dto.messaginservice.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberPayload extends Payload {
    @NotNull
    public Long memberId;
    @NotNull
    public Long chatId;
}
