package com.yazikochesalna.authorizationservice.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class AuthRequestDto(
    @NotNull
    @NotEmpty
    val login: String,
    @NotNull
    @NotEmpty
    val password: String
)