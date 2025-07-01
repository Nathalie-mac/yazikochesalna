package com.yazikochesalna.authorizationservice.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import lombok.*
import org.springframework.data.domain.Persistable
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


@Entity(name = "user_auth")
@NoArgsConstructor
@AllArgsConstructor
@Builder
data class UserAuth(
    @Id
    var id: Long = -1,
    @NotNull
    @NotEmpty
    var login: String = "",
    @NotNull
    @NotEmpty
    var password: String= ""
) {


}