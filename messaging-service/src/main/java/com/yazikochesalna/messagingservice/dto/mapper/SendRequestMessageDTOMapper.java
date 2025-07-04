package com.yazikochesalna.messagingservice.dto.mapper;

import com.yazikochesalna.messagingservice.dto.messaging.request.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.dto.storage.MessageToStorageDTO;
import com.yazikochesalna.messagingservice.dto.storage.MessageType;
import com.yazikochesalna.messagingservice.dto.storage.PayloadMessageToStorageDTO;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class SendRequestMessageDTOMapper {
    
    public MessageToStorageDTO toMessageToStorageDTO(SendRequestMessageDTO sendRequestMessageDTO, Long userId) {
       PayloadMessageToStorageDTO payloadMessageToStorageDTO = new PayloadMessageToStorageDTO();
       payloadMessageToStorageDTO.setMessageId(UUID.randomUUID());
       payloadMessageToStorageDTO.setSenderId(userId);
       payloadMessageToStorageDTO.setChatId(sendRequestMessageDTO.getChatId());
       payloadMessageToStorageDTO.setText(sendRequestMessageDTO.getMessage());
       payloadMessageToStorageDTO.setTimestamp(Instant.now());

       MessageToStorageDTO messageToStorageDTO = new MessageToStorageDTO();
       messageToStorageDTO.setType(MessageType.MESSAGE);
       messageToStorageDTO.setMessage(payloadMessageToStorageDTO);
       return messageToStorageDTO;
    }
}