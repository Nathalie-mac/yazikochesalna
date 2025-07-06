package com.yazikochesalna.messagingservice.dto.mapper;

import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadMessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.notification.PayloadReceiveMessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.notification.ReceiveMessageDTO;
import org.springframework.stereotype.Component;

@Component
public class MessageDTOMapper {

    public ReceiveMessageDTO toReceiveMessageDTO(MessageDTO messageDTO) {
        PayloadMessageDTO payloadMessageDTO = messageDTO.getMessage();
        PayloadReceiveMessageDTO payloadReceiveMessageDTO = new PayloadReceiveMessageDTO();
        payloadReceiveMessageDTO.setMessageId(payloadMessageDTO.getMessageId());
        payloadReceiveMessageDTO.setSenderId(payloadMessageDTO.getSenderId());
        payloadReceiveMessageDTO.setText(payloadMessageDTO.getText());
        payloadReceiveMessageDTO.setTimestamp(payloadMessageDTO.getTimestamp());

        ReceiveMessageDTO receiveMessageDTO = new ReceiveMessageDTO();
        receiveMessageDTO.setChatId(payloadMessageDTO.getChatId());
        receiveMessageDTO.setMessage(payloadReceiveMessageDTO);


        return receiveMessageDTO;
    }
}