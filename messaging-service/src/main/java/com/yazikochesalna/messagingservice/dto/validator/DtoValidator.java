package com.yazikochesalna.messagingservice.dto.validator;

import com.yazikochesalna.messagingservice.dto.messaging.notification.ActionType;
import com.yazikochesalna.messagingservice.dto.messaging.request.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.exception.InvalidSendMessageFormatCustomException;

public class DtoValidator {


    public static void validateSendRequestMessageDTO(SendRequestMessageDTO dto) {
        if (dto == null) {
            throw new InvalidSendMessageFormatCustomException();
        }
        if (isInvalidSendRequestMessageDTOFields(dto)) {
            throw new InvalidSendMessageFormatCustomException();
        }
    }

    private static boolean isInvalidSendRequestMessageDTOFields(SendRequestMessageDTO dto) {
        return (!dto.getAction().equals(ActionType.SEND))
                || dto.getChatId() == null
                || dto.getMessage() == null
                || dto.getMessage().trim().isEmpty();
    }
}