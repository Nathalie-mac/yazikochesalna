package com.yazikochesalna.messagingservice.dto.kafka;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yazikochesalna.messagingservice.dto.deserializer.MessageDTODeserializer;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@JsonDeserialize(using = MessageDTODeserializer.class)
@Accessors(chain = true)
public class MessageDTO {
    private MessageType type;
    @Builder.Default
    private UUID messageId = UUID.randomUUID();
    @Builder.Default
    private Instant timestamp = Instant.now();
    @Valid
    private PayloadDTO payload;


    public MessageDTO(AwaitingResponseMessageDTO awaitingResponseMessageDTO) {
        this.type = awaitingResponseMessageDTO.getType();
        this.messageId = awaitingResponseMessageDTO.getMessageId();
        this.timestamp = awaitingResponseMessageDTO.getTimestamp();
        this.payload = awaitingResponseMessageDTO.getPayload();
    }

    public <T extends PayloadDTO> T getPayload() {
        return (T) payload;
    }


}
