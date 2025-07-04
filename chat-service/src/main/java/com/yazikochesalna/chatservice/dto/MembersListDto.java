package com.yazikochesalna.chatservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record MembersListDto (
    @NotNull
    List<Long> usersIds){

    @JsonIgnore
    public boolean isEmpty(){
        return usersIds.isEmpty();
    }
}
