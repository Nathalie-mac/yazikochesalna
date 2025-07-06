package com.yazikochesalna.messagingservice.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import com.yazikochesalna.messagingservice.dto.kafka.MessageType;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadDTO;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadMessageDTO;

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

    @Override
    public MessageDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        JsonNode typeNode = node.get("type");
        if (typeNode == null) {
            throw new IOException("Field 'type' is missing in JSON");
        }
        String typeStr = typeNode.asText();
        MessageType type = MessageType.valueOf(typeStr);

        UUID messageId = UUID.fromString(node.get("messageId").asText());
        JsonNode timestampNode = node.get("timestamp");
        Instant timestamp;
        if (timestampNode.isNumber()) {
            double timestampValue = timestampNode.asDouble();
            long seconds = (long) timestampValue;
            long nanos = (long) ((timestampValue - seconds) * 1_000_000_000);
            timestamp = Instant.ofEpochSecond(seconds, nanos);
        } else {
            throw new IOException("Field 'timestamp' must be a number");
        }
        JsonNode payloadNode = node.get("payload");

        PayloadDTO payload;
        if (type == MessageType.MESSAGE) {
            payload = mapper.treeToValue(payloadNode, PayloadMessageDTO.class);
        } else {
            throw new IOException("Unsupported MessageType: " + type);
        }

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType(type);
        messageDTO.setMessageId(messageId);
        messageDTO.setTimestamp(timestamp);
        messageDTO.setPayload(payload);

        return messageDTO;
    }
}
