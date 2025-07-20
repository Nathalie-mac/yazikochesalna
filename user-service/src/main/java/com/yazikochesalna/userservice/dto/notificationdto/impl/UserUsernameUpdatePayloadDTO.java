package com.yazikochesalna.userservice.dto.notificationdto.impl;

import com.yazikochesalna.userservice.dto.notificationdto.UserPayloadDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUsernameUpdatePayloadDTO extends UserPayloadDTO {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;
}
