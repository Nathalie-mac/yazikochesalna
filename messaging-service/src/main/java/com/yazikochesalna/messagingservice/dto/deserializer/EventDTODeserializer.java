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

public class EventDTODeserializer extends StdDeserializer<EventDTO> {

    public EventDTODeserializer() {
        this(EventDTO.class);
    }

    public EventDTODeserializer(Class<?> vc) {
        super(vc);
    }


    @Override
    public EventDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        EventType type = DTODeserializer.getMessageType(node);

        PayloadDTO payload = DTODeserializer.getPayload(mapper, type, node);

        EventDTO.EventDTOBuilder builder = EventDTO.builder()
                .type(type)
                .payload(payload);

        if (type == EventType.NEW_USER_AVATAR || type == EventType.NEW_USERNAME) {
            builder.messageId(null);
        }

        EventDTO eventDTO = builder.build();


        if (node.get("messageId") != null) {
            eventDTO.setMessageId(DTODeserializer.getMessageId(node));
        }
        if (node.get("timestamp") != null) {
            eventDTO.setTimestamp(DTODeserializer.getTime(node));
        }

        return eventDTO;
    }


}
