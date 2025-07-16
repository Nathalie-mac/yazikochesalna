package com.yazikochesalna.messagingservice.dto.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadNotificationPinDTO extends PayloadDTO{
    @NotNull(message = "pinMessageId не может быть null")
    private UUID pinMessageId;

    private Long memberId;
    @NotNull(message = "chatId не может быть null")
    private Long chatId;
}
