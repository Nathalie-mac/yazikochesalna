package com.yazikochesalna.messagingservice.dto.events.payload.user.impl;

import com.yazikochesalna.messagingservice.dto.events.payload.user.UserPayloadDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUsernameUpdatePayloadDTO extends UserPayloadDTO {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;
}
