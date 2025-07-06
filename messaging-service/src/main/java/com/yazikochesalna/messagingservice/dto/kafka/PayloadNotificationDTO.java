package com.yazikochesalna.messagingservice.dto.kafka;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadNotificationDTO extends PayloadDTO {
    private Long memberId;
    private Long chatId;
}
