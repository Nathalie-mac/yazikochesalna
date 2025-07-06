package com.yazikochesalna.messagingservice.dto.mapper;

import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import com.yazikochesalna.messagingservice.dto.kafka.MessageType;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadMessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.request.SendRequestMessageDTO;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class SendRequestMessageDTOMapper {

    public MessageDTO toMessageToStorageDTO(SendRequestMessageDTO sendRequestMessageDTO, Long userId) {
        PayloadMessageDTO payloadMessageDTO = new PayloadMessageDTO();
        payloadMessageDTO.setMessageId(UUID.randomUUID());
        payloadMessageDTO.setSenderId(userId);
        payloadMessageDTO.setChatId(sendRequestMessageDTO.getChatId());
        payloadMessageDTO.setText(sendRequestMessageDTO.getMessage());
        payloadMessageDTO.setTimestamp(Instant.now());

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType(MessageType.MESSAGE);
        messageDTO.setMessage(payloadMessageDTO);
        return messageDTO;
    }
}