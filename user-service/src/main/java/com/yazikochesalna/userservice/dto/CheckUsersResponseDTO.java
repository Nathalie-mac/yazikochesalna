package com.yazikochesalna.userservice.dto;

import java.util.List;

public record CheckUsersResponseDTO (
        List<Long> existingUsersIds
){
}
