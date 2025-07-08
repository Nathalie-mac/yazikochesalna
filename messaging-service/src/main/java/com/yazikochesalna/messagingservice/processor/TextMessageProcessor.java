package com.yazikochesalna.messagingservice.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.kafka.AwaitingResponseMessageDTO;
import com.yazikochesalna.messagingservice.dto.response.ResponseResultType;
import com.yazikochesalna.messagingservice.dto.validator.MessageDTOValidator;
import com.yazikochesalna.messagingservice.exception.InvalidMessageFormatCustomException;
import com.yazikochesalna.messagingservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
@RequiredArgsConstructor
public class TextMessageProcessor {
    private final WebSocketService webSocketService;
    private final MessageDTOValidator messageDTOValidator;
    private final ObjectMapper objectMapper;


    public void processMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            var messageDTO = objectMapper.readValue(payload, AwaitingResponseMessageDTO.class);
            messageDTOValidator.validate(messageDTO);
            webSocketService.sendMessage(session, messageDTO);

        } catch (JsonProcessingException | InvalidMessageFormatCustomException e) {
            System.err.println("Ошибка обработки сообщения:" + e.getMessage());
            webSocketService.sendErrorResponse(session, ResponseResultType.INVALID_FORMAT, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


