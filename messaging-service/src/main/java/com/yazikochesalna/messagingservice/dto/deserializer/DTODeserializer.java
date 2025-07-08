package com.yazikochesalna.messagingservice.dto.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.kafka.MessageType;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadDTO;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadMessageDTO;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadNotificationDTO;
import com.yazikochesalna.messagingservice.exception.InvalidMessageFormatCustomException;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public class DTODeserializer {
    public static Instant getTime(JsonNode node) {
        JsonNode timestampNode = node.get("timestamp");
        Instant timestamp;
        if (timestampNode.isNumber()) {
            double timestampValue = timestampNode.asDouble();
            long seconds = (long) timestampValue;
            long nanos = (long) ((timestampValue - seconds) * 1_000_000_000);
            timestamp = Instant.ofEpochSecond(seconds, nanos);
        } else {
            throw new InvalidMessageFormatCustomException();
        }
        return timestamp;
    }

    public static PayloadDTO getPayload(ObjectMapper mapper, MessageType type, JsonNode node) throws IOException {
        JsonNode payloadNode = node.get("payload");
        PayloadDTO payload;
        if (type == MessageType.MESSAGE) {
            payload = mapper.treeToValue(payloadNode, PayloadMessageDTO.class);
        } else if (type == MessageType.NEW_MEMBER || type == MessageType.DROP_MEMBER) {
            payload = mapper.treeToValue(payloadNode, PayloadNotificationDTO.class);
        } else {
            throw new InvalidMessageFormatCustomException();
        }
        return payload;
    }


    public static MessageType getMessageType(JsonNode node) throws IOException {
        JsonNode typeNode = node.get("type");
        if (typeNode == null) {
            throw new IOException("Field 'type' is missing in JSON");
        }
        String typeStr = typeNode.asText();
        MessageType type;
        try {
            type = MessageType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            throw new InvalidMessageFormatCustomException();
        }
        return type;
    }

    public static UUID getMessageId(JsonNode node) {
        return UUID.fromString(node.get("messageId").asText());
    }
}
