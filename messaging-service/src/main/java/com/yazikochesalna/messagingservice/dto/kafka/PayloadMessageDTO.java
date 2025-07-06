package com.yazikochesalna.messagingservice.dto.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PayloadMessageDTO extends PayloadDTO {
    private Long senderId;
    private Long chatId;
    private String text;
}
