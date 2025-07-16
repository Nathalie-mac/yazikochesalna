package com.yazikochesalna.messagingservice.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.yazikochesalna.messagingservice.dto.kafka.MessageType;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadDTO;
import com.yazikochesalna.messagingservice.dto.request.AwaitingResponseMessageDTO;

import java.io.IOException;

public class AwaitingResponseMessageDTODeserializer extends StdDeserializer<AwaitingResponseMessageDTO> {

    public AwaitingResponseMessageDTODeserializer() {
        this(AwaitingResponseMessageDTO.class);
    }

    public AwaitingResponseMessageDTODeserializer(Class<?> vc) {
        super(vc);
    }


    @Override
    public AwaitingResponseMessageDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        MessageType type = DTODeserializer.getMessageType(node);

        PayloadDTO payload = DTODeserializer.getPayload(mapper, type, node);

        var messageDTO = AwaitingResponseMessageDTO.builder()
                .type(type)
                .payload(payload)
                .build();


        if (node.get("requestId") != null) {
            Long requestId = Long.valueOf(node.get("requestId").asText());
            messageDTO.setRequestId(requestId);
        }


        return messageDTO;
    }


}
