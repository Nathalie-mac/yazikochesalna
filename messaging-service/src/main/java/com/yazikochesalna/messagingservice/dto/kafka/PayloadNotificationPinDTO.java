package com.yazikochesalna.messagingservice.dto.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadNotificationPinDTO extends PayloadDTO {
    @NotNull(message = "pinMessageId не может быть null")
    private UUID pinMessageId;

    private Long memberId;

}
