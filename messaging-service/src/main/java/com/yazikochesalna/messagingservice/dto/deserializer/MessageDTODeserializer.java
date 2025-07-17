package com.yazikochesalna.messagingservice.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.yazikochesalna.messagingservice.dto.events.EventDTO;
import com.yazikochesalna.messagingservice.dto.events.EventType;
import com.yazikochesalna.messagingservice.dto.events.payload.PayloadDTO;

import java.io.IOException;

public class MessageDTODeserializer extends StdDeserializer<EventDTO> {

    public MessageDTODeserializer() {
        this(EventDTO.class);
    }

    public MessageDTODeserializer(Class<?> vc) {
        super(vc);
    }


    @Override
    public EventDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        EventType type = DTODeserializer.getMessageType(node);

        PayloadDTO payload = DTODeserializer.getPayload(mapper, type, node);


        var messageDTO = EventDTO.builder()
                .type(type)
                .payload(payload)
                .build();

        if (node.get("messageId") != null) {
            messageDTO.setMessageId(DTODeserializer.getMessageId(node));
        }
        if (node.get("timestamp") != null) {
            messageDTO.setTimestamp(DTODeserializer.getTime(node));
        }

        return messageDTO;
    }


}
