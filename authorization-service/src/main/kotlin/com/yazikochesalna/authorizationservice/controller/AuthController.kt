package com.yazikochesalna.authorizationservice.controller

import com.yazikochesalna.authorizationservice.com.yazikochesalna.authorizationservice.dto.GetLoginResponseDto
import com.yazikochesalna.authorizationservice.com.yazikochesalna.authorizationservice.dto.RegisterRequestDto
import com.yazikochesalna.authorizationservice.dto.AuthRequestDto
import com.yazikochesalna.authorizationservice.dto.TokenResponseDto
import com.yazikochesalna.authorizationservice.exception.AuthException
import com.yazikochesalna.authorizationservice.service.AuthService
import com.yazikochesalna.common.authentication.JwtAuthenticationToken
import com.yazikochesalna.common.service.JwtService
import io.swagger.v3.oas.annotations.Hidden
import jakarta.annotation.security.RolesAllowed
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val autService: AuthService,
) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequestDto): ResponseEntity<TokenResponseDto> {
        if (autService.isUserRegistered(request.login)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        val tokens = autService.registerUser(
            login = request.login,
            username = request.username,
            password = request.password,
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(tokens)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequestDto): ResponseEntity<TokenResponseDto> =
        ResponseEntity.ok(autService.loginUser(request.login, request.password))

    @PostMapping("/refresh")
    fun refresh(): ResponseEntity<TokenResponseDto> {
        SecurityContextHolder.getContext().authentication?.let {
            if (it !is JwtAuthenticationToken) {
                throw AuthException("Invalid authentication")
            }
            return ResponseEntity.ok(autService.refreshTokens(it.userId))
        }
        throw AuthException("Invalid authentication")
    }

    @GetMapping("/getlogin")
    @RolesAllowed("SERVICE")
    @Hidden
    fun getLogin(userID: Long): ResponseEntity<GetLoginResponseDto> =
        ResponseEntity.ok(GetLoginResponseDto(autService.getUserLogin(userID)))
}

