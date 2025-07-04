package com.yazikochesalna.messagingservice.dto.messaging.response;

import com.yazikochesalna.messagingservice.dto.messaging.notification.ActionType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendResponseMessageDTO {

    private final ActionType action = ActionType.SEND_RESPONSE;
    private Long requestId;
    private SendResponseResultType result;

}