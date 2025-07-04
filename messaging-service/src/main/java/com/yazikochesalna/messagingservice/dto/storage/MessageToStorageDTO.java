package com.yazikochesalna.messagingservice.dto.storage;

import lombok.Data;

@Data
public class MessageToStorageDTO {
    private MessageType type;
    private PayloadMessageToStorageDTO message;

}
