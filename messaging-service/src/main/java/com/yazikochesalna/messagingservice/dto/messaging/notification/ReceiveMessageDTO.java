package com.yazikochesalna.messagingservice.dto.messaging.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ReceiveMessageDTO {
    private final ActionType action = ActionType.NEW_MESSAGE;;
    private Long chatId;
    private MessageDTO message;

    public ReceiveMessageDTO() {

    }
}
