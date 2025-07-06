package com.yazikochesalna.messagingservice.dto.messaging.request;

import com.yazikochesalna.messagingservice.dto.messaging.notification.ActionType;
import lombok.Data;

@Data
public class SendRequestMessageDTO {
    private final ActionType action = ActionType.SEND;
    private Long chatId;
    private String message;

}