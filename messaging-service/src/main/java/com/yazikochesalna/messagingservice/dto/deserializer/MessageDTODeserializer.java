package com.yazikochesalna.messagingservice.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.yazikochesalna.messagingservice.dto.kafka.*;
import com.yazikochesalna.messagingservice.exception.InvalidMessageFormatCustomException;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public class MessageDTODeserializer extends StdDeserializer<MessageDTO> {

    public MessageDTODeserializer() {
        this(MessageDTO.class);
    }

    public MessageDTODeserializer(Class<?> vc) {
        super(vc);
    }

    private static Instant getInstant(JsonNode timestampNode) {
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

    private static PayloadDTO getPayload(ObjectMapper mapper, MessageType type, JsonNode payloadNode) throws IOException {
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

    private static MessageType getMessageType(String typeStr) {
        MessageType type = null;
        try {
            type = MessageType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            throw new InvalidMessageFormatCustomException();
        }
        return type;
    }

    @Override
    public MessageDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        JsonNode typeNode = node.get("type");
        if (typeNode == null) {
            throw new IOException("Field 'type' is missing in JSON");
        }
        String typeStr = typeNode.asText();
        MessageType type = getMessageType(typeStr);

        JsonNode payloadNode = node.get("payload");

        PayloadDTO payload = getPayload(mapper, type, payloadNode);
        var messageDTO = MessageDTO.builder()
                .type(type)
                .payload(payload)
                .build();

        if (node.get("messageId") != null) {
            UUID messageId = UUID.fromString(node.get("messageId").asText());
            messageDTO.setMessageId(messageId);
        }

        if (node.get("timestamp") != null) {
            JsonNode timestampNode = node.get("timestamp");
            Instant timestamp = getInstant(timestampNode);
            messageDTO.setTimestamp(timestamp);
        }


        return messageDTO;
    }


}
