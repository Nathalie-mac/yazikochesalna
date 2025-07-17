package com.yazikochesalna.messagingservice.dto.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.events.EventType;
import com.yazikochesalna.messagingservice.dto.events.payload.PayloadDTO;
import com.yazikochesalna.messagingservice.dto.events.payload.chat.impl.ChatAvatarPayloadDTO;
import com.yazikochesalna.messagingservice.dto.events.payload.chat.impl.ChatMemberUpdatePayloadDTO;
import com.yazikochesalna.messagingservice.dto.events.payload.chat.impl.ChatMessagePayloadDTO;
import com.yazikochesalna.messagingservice.dto.events.payload.chat.impl.ChatPinnedMessagePayloadDTO;
import com.yazikochesalna.messagingservice.exception.InvalidMessageFormatCustomException;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class DTODeserializer {
    private static final Long NANOSECONDS_IN_SECOND = 1_000_000_000L;

    private static final Map<EventType, Class<? extends PayloadDTO>> MESSAGE_TYPE_TO_DTO = Map.of(
            EventType.MESSAGE, ChatMessagePayloadDTO.class,
            EventType.NEW_MEMBER, ChatMemberUpdatePayloadDTO.class,
            EventType.DROP_MEMBER, ChatMemberUpdatePayloadDTO.class,
            EventType.PIN, ChatPinnedMessagePayloadDTO.class,
            EventType.NEW_CHAT_AVATAR, ChatAvatarPayloadDTO.class
    );

    public static Instant getTime(JsonNode node) {
        JsonNode timestampNode = node.get("timestamp");
        if (timestampNode.isNumber()) {
            double timestampValue = timestampNode.asDouble();
            long seconds = (long) timestampValue;
            long nanos = (long) ((timestampValue - seconds) * NANOSECONDS_IN_SECOND);
            return Instant.ofEpochSecond(seconds, nanos);
        } else {
            throw new InvalidMessageFormatCustomException();
        }

    }

    public static PayloadDTO getPayload(ObjectMapper mapper, EventType type, JsonNode node) throws IOException {
        JsonNode payloadNode = node.get("payload");

        Class<? extends PayloadDTO> dtoClass = MESSAGE_TYPE_TO_DTO.get(type);

        if (dtoClass == null) {
            throw new InvalidMessageFormatCustomException();
        }

        return mapper.treeToValue(payloadNode, dtoClass);
    }


    public static EventType getMessageType(JsonNode node) throws IOException {
        JsonNode typeNode = node.get("type");
        if (typeNode == null) {
            throw new IOException("Field 'type' is missing in JSON");
        }
        String typeStr = typeNode.asText();
        EventType type;
        try {
            type = EventType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            throw new InvalidMessageFormatCustomException();
        }
        return type;
    }

    public static UUID getMessageId(JsonNode node) {
        return UUID.fromString(node.get("messageId").asText());
    }
}
