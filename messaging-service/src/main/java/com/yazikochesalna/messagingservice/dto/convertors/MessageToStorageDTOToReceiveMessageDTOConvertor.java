package com.yazikochesalna.messagingservice.dto.convertors;

import com.yazikochesalna.messagingservice.dto.MessageDTO;
import com.yazikochesalna.messagingservice.dto.ReceiveMessageDTO;
import com.yazikochesalna.messagingservice.dto.storage.MessageToStorageDTO;

public class MessageToStorageDTOToReceiveMessageDTOConvertor {
    public static ReceiveMessageDTO convert(MessageToStorageDTO messageToStorageDTO) {
        MessageDTO messageDTO = new MessageDTO(messageToStorageDTO.getMessageId(),
                messageToStorageDTO.getSenderId(),
                messageToStorageDTO.getText(),
                messageToStorageDTO.getTimestamp());
        return new ReceiveMessageDTO(
                messageToStorageDTO.getChatId(),
                messageDTO);
    }
}
