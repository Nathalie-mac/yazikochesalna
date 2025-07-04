package com.yazikochesalna.messagingservice.dto.mapper;

import com.yazikochesalna.messagingservice.dto.messaging.notification.MessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.notification.ReceiveMessageDTO;
import com.yazikochesalna.messagingservice.dto.storage.MessageToStorageDTO;
import com.yazikochesalna.messagingservice.dto.storage.PayloadMessageToStorageDTO;
import org.springframework.stereotype.Component;

@Component
public class MessageToStorageDTOMapper {
    
    public ReceiveMessageDTO toReceiveMessageDTO(MessageToStorageDTO messageToStorageDTO) {
        PayloadMessageToStorageDTO payloadMessageToStorageDTO = messageToStorageDTO.getMessage();
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageId(payloadMessageToStorageDTO.getMessageId());
        messageDTO.setSenderId(payloadMessageToStorageDTO.getSenderId());
        messageDTO.setText(payloadMessageToStorageDTO.getText());
        messageDTO.setTimestamp(payloadMessageToStorageDTO.getTimestamp());

        ReceiveMessageDTO receiveMessageDTO = new ReceiveMessageDTO();
        receiveMessageDTO.setChatId(payloadMessageToStorageDTO.getChatId());
        receiveMessageDTO.setMessage(messageDTO);

        
        return receiveMessageDTO;
    }
}