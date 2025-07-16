package com.yazikochesalna.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileUpdateRequestDTO {

    @NotNull
    private Long userID;

    @NotNull
    private UUID fileUUID;
}
