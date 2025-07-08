package com.yazikochesalna.chatservice.dto.members;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Set;

@Builder
public record AddMembersRequest(
        @NotNull
        Set<Long> newMembersIds
) {
}
