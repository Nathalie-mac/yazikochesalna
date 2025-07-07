package com.yazikochesalna.messagingservice.dto.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadNotificationDTO extends PayloadDTO {
    @NotNull(message = "memberId не может быть null")
    private Long memberId;
    @NotNull(message = "chatId не может быть null")
    private Long chatId;
}
