package com.yazikochesalna.authorizationservice.controller

import com.yazikochesalna.authorizationservice.entity.UserAuth
import com.yazikochesalna.authorizationservice.service.UserService
import com.yazikochesalna.authorizationservice.exception.AuthException
import com.yazikochesalna.authorizationservice.dto.AuthRequestDto
import com.yazikochesalna.authorizationservice.dto.TokenResponseDto
import com.yazikochesalna.common.authentication.JwtAuthenticationToken
import com.yazikochesalna.common.service.JwtService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping("/register")
    fun register(@RequestBody request: AuthRequestDto): ResponseEntity<TokenResponseDto> {
        if (userService.isUserRegistered(request.username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        val user = userService.createUser(
            request.username,
            passwordEncoder.encode(request.password)
        )

        authManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        val token = jwtService.generateAccessToken(user.id)
        return ResponseEntity.ok(TokenResponseDto(token))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequestDto): ResponseEntity<TokenResponseDto> {
        //todo check password
        val userAuth = userService.loadUserByUsername(request.username) as UserAuth
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        val token = jwtService.generateAccessToken(userAuth.id)
        return ResponseEntity.ok(TokenResponseDto(token))
    }

    @PostMapping("/refresh")
    fun refresh(): ResponseEntity<TokenResponseDto> {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication is JwtAuthenticationToken) {
            val newToken = jwtService.generateRefreshToken(authentication.userId)
            return ResponseEntity.ok(TokenResponseDto(newToken))
        }
        throw AuthException("Invalid authentication")
    }
}

