package com.yazikochesalna.chatservice.dto.messaginservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Builder
public record ListUsersDto(
        Set<Long> userIds
) {
}
