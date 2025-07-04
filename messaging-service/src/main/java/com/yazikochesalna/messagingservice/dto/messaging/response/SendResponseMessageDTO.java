package com.yazikochesalna.messagingservice.dto.messaging.response;

import com.yazikochesalna.messagingservice.dto.messaging.notification.ActionType;
import lombok.Data;
import lombok.Getter;

@Data
public class SendResponseMessageDTO {

    private final ActionType action = ActionType.SEND_RESPONSE;
    private Long requestId;
    private SendResponseResultType result;

}