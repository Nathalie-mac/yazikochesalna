package com.yazikochesalna.messagingservice.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.yazikochesalna.messagingservice.dto.events.AwaitingResponseEventDTO;
import com.yazikochesalna.messagingservice.dto.events.EventType;
import com.yazikochesalna.messagingservice.dto.events.payload.PayloadDTO;

import java.io.IOException;

public class AwaitingResponseMessageDTODeserializer extends StdDeserializer<AwaitingResponseEventDTO> {

    public AwaitingResponseMessageDTODeserializer() {
        this(AwaitingResponseEventDTO.class);
    }

    public AwaitingResponseMessageDTODeserializer(Class<?> vc) {
        super(vc);
    }


    @Override
    public AwaitingResponseEventDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        EventType type = DTODeserializer.getMessageType(node);

        PayloadDTO payload = DTODeserializer.getPayload(mapper, type, node);

        AwaitingResponseEventDTO messageDTO = new AwaitingResponseEventDTO();
        messageDTO.setType(type)
                .setPayload(payload);


        if (node.get("requestId") != null) {
            Long requestId = Long.valueOf(node.get("requestId").asText());
            messageDTO.setRequestId(requestId);
        }


        return messageDTO;
    }


}
