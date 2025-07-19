package com.yazikochesalna.messagingservice.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class UsersResponseDTO {
    private List<Long> userIds;
}
