package com.yazikochesalna.chatservice.dto.messagestorageservice;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record MessageDto (
        UUID messageId,
        String type,
        Instant timestamp,
        PayloadDto payload
){

}
