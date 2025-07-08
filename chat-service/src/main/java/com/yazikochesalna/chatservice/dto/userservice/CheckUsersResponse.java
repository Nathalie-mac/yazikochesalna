package com.yazikochesalna.chatservice.dto.userservice;

import java.util.Set;

public record CheckUsersResponse(
        Set<Long> existingUsersIds
) {
}
