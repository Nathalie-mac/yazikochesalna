package com.yazikochesalna.messagingservice.dto.kafka;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yazikochesalna.messagingservice.deserializer.MessageDTODeserializer;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@JsonDeserialize(using = MessageDTODeserializer.class)
public class MessageDTO {
    private MessageType type;
    private UUID messageId;
    private Instant timestamp;
    private PayloadDTO payload;


    public <T extends PayloadDTO> T getPayload() {
        return (T) payload;
    }


}
