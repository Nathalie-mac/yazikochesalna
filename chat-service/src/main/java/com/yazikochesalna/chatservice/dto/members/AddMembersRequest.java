package com.yazikochesalna.chatservice.dto.members;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record AddMembersRequest(
        @NotNull
        List<Long> newMembersIds
) {
}
