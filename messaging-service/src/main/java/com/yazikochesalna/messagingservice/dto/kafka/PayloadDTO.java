package com.yazikochesalna.messagingservice.dto.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class PayloadDTO {
    @NotNull(message = "chatId не может быть null")
    private Long chatId;

}
