package com.yazikochesalna.messagingservice.dto.messaging.request;

import com.yazikochesalna.messagingservice.dto.messaging.notification.ActionType;
import lombok.Data;
import lombok.Getter;

@Data
public class SendRequestMessageDTO {
    private final ActionType action = ActionType.SEND;
    private Long requestId;
    private Long chatId;
    private String message;

}