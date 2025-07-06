package com.yazikochesalna.messagingservice.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.messaging.notification.ActionType;
import com.yazikochesalna.messagingservice.dto.messaging.request.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.dto.validator.DtoValidator;
import com.yazikochesalna.messagingservice.exception.InvalidSendMessageFormatCustomException;
import com.yazikochesalna.messagingservice.service.WebSocketService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Component
public class TextMessageProcessor {
    public static final String ACTION_FIELD_NAME = "action";
    private final WebSocketService webSocketService;

    private final ObjectMapper objectMapper;

    public TextMessageProcessor(WebSocketService webSocketService, ObjectMapper objectMapper) {
        this.webSocketService = webSocketService;
        this.objectMapper = objectMapper;
    }

    public void processMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            Map<String, String> requestMap = objectMapper.readValue(payload, Map.class);
            String action = requestMap.get(ACTION_FIELD_NAME);

            if (action == null) {
                throw new InvalidSendMessageFormatCustomException();
            }

            if (ActionType.SEND.toString()
                    .equals(requestMap.get(ACTION_FIELD_NAME))) {

                SendRequestMessageDTO sendMessage = objectMapper.readValue(message.getPayload(), SendRequestMessageDTO.class);
                DtoValidator.validateSendRequestMessageDTO(sendMessage);
                webSocketService.sendMessage(session, sendMessage);

            }
        } catch (JsonProcessingException | InvalidSendMessageFormatCustomException e) {
            System.err.println("Ошибка обработки сообщения:" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
