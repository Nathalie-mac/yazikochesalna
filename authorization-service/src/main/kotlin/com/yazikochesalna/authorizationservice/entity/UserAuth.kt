package com.yazikochesalna.authorizationservice.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import lombok.*


@Entity(name = "user_auth")
@Builder
class UserAuth(
    @Id
    var id: Long = -1,
    @NotNull
    @NotEmpty
    var login: String = "",
    @NotNull
    @NotEmpty
    var password: String= ""
)