package com.yazikochesalna.messagingservice.dto.validator;

import com.yazikochesalna.messagingservice.dto.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.exception.InvalidSendMessageFormatException;

public final class DtoValidator {


    public static void validateSendRequestMessageDTO(SendRequestMessageDTO dto) {
        if (dto == null) {
            throw new InvalidSendMessageFormatException();
        }
        if (dto.getRequestId() == null
                || dto.getAction() == null
                || dto.getAction().trim().isEmpty()
                || dto.getChatId() == null
                || dto.getMessage() == null
                || dto.getMessage().trim().isEmpty()) {
            throw new InvalidSendMessageFormatException();
        }
    }
}