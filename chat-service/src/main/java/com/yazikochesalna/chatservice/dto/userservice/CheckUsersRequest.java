package com.yazikochesalna.chatservice.dto.userservice;

import java.util.Set;

public record CheckUsersRequest(
        Set<Long> usersIds
) {
}
