package com.yazikochesalna.messagingservice.dto.kafka;

import lombok.Data;

@Data
public class MessageDTO {
    private MessageType type;
    private PayloadMessageDTO message;

}
