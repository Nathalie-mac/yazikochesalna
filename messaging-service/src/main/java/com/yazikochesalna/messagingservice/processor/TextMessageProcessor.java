package com.yazikochesalna.messagingservice.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import com.yazikochesalna.messagingservice.exception.InvalidSendMessageFormatCustomException;
import com.yazikochesalna.messagingservice.service.WebSocketService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class TextMessageProcessor {
    private final WebSocketService webSocketService;

    private final ObjectMapper objectMapper;

    public TextMessageProcessor(WebSocketService webSocketService, ObjectMapper objectMapper) {
        this.webSocketService = webSocketService;
        this.objectMapper = objectMapper;
    }

    public void processMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            MessageDTO messageDTO = objectMapper.readValue(payload, MessageDTO.class);
            webSocketService.sendMessageToKafka(session, messageDTO);


        } catch (JsonProcessingException | InvalidSendMessageFormatCustomException e) {
            System.err.println("Ошибка обработки сообщения:" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
