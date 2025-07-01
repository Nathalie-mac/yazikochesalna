package com.yazikochesalna.messagingservice.dto.validator;

import com.yazikochesalna.messagingservice.dto.MessageDTO;
import com.yazikochesalna.messagingservice.dto.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.exception.InvalidSendMessageFormatException;

public final class DtoValidator {


    public static void validateSendRequestMessageDTO(SendRequestMessageDTO dto) {
        validateMessageDTO(dto);

        if (dto.getChatId() == null
                || dto.getMessage() == null
                || dto.getMessage().trim().isEmpty()) {
            throw new InvalidSendMessageFormatException();
        }
    }

    private static void validateMessageDTO(MessageDTO dto) {
        if (dto == null) {
            throw new InvalidSendMessageFormatException();
        }

        if (dto.getRequestId() == null
                || dto.getAction() == null
                || dto.getAction().trim().isEmpty()) {
            throw new InvalidSendMessageFormatException();
        }
    }
}