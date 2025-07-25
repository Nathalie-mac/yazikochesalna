package com.yazikochesalna.userservice.dto.notificationdto.impl;

import com.yazikochesalna.userservice.dto.notificationdto.UserPayloadDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UserAvatarUpdatePayloadDTO extends UserPayloadDTO {
    @NotNull(message = "avatarId не может быть null")
    private UUID avatarId;
}
