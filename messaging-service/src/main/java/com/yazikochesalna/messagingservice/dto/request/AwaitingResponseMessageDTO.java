package com.yazikochesalna.messagingservice.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yazikochesalna.messagingservice.dto.deserializer.AwaitingResponseMessageDTODeserializer;
import com.yazikochesalna.messagingservice.dto.kafka.MessageType;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadDTO;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonDeserialize(using = AwaitingResponseMessageDTODeserializer.class)
@Accessors(chain = true)
public class AwaitingResponseMessageDTO {
    private MessageType type;
    private Long requestId;
    @Builder.Default
    private UUID messageId = UUID.randomUUID();
    @Builder.Default
    private Instant timestamp = Instant.now();
    @Valid
    private PayloadDTO payload;


    public <T extends PayloadDTO> T getPayload() {
        return (T) payload;
    }


}
