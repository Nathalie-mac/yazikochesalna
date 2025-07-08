package com.yazikochesalna.userservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CheckUsersRequestDTO(
        @NotNull
        List<Long> usersIds
) {
}
