package com.yazikochesalna.messagingservice.dto.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yazikochesalna.messagingservice.dto.deserializer.MessageDTODeserializer;
import com.yazikochesalna.messagingservice.dto.events.payload.PayloadDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonDeserialize(using = MessageDTODeserializer.class)
@Accessors(chain = true)
public class EventDTO {
    protected EventType type;
    @Builder.Default
    protected UUID messageId = UUID.randomUUID();
    @Builder.Default
    protected Instant timestamp = Instant.now();
    @Valid
    protected PayloadDTO payload;


//    public EventDTO(AwaitingResponseEventDTO awaitingResponseEventDTO) {
//        this.type = awaitingResponseEventDTO.getType();
//        this.messageId = awaitingResponseEventDTO.getMessageId();
//        this.timestamp = awaitingResponseEventDTO.getTimestamp();
//        this.payload = awaitingResponseEventDTO.getPayload();
//    }

    public <T extends PayloadDTO> T getPayload() {
        return (T) payload;
    }


}
