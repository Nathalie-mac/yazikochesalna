package com.yazikochesalna.messagingservice.dto.events.payload.user;

import com.yazikochesalna.messagingservice.dto.events.payload.PayloadDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserPayloadDTO extends PayloadDTO {
    @NotNull(message = "userId не может быть null")
    protected Long userId;

}
