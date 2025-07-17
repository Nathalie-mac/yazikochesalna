package com.yazikochesalna.messagingservice.dto.events.payload.user.impl;

import com.yazikochesalna.messagingservice.dto.events.payload.user.UserPayloadDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UserAvatarUpdatePayloadDTO extends UserPayloadDTO {
    @NotNull(message = "avatarId не может быть null")
    private UUID avatarId;
}
