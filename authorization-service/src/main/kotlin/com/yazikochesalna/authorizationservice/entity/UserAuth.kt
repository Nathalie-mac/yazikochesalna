package com.yazikochesalna.authorizationservice.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


@Entity(name = "user_auth")
class UserAuth(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val login: String,
    private val password: String
) : UserDetails {


    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun getPassword(): String = password
    override fun getUsername(): String = login
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}