package com.yazikochesalna.messagingservice.dto.validator;

import com.yazikochesalna.messagingservice.dto.events.AwaitingResponseEventDTO;
import com.yazikochesalna.messagingservice.exception.InvalidMessageFormatCustomException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class MessageDTOValidator {
    private final Validator validator;


    public void validate(AwaitingResponseEventDTO messageDTO) {
        Set<ConstraintViolation<AwaitingResponseEventDTO>> violations = validator.validate(messageDTO);
        if (!violations.isEmpty()) {
            violations.forEach(violation ->
                    System.err.println(violation.getPropertyPath() + ": " + violation.getMessage()));
            throw new InvalidMessageFormatCustomException();
        }
    }
}