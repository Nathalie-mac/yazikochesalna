package com.yazikochesalna.authorizationservice.controller

import com.yazikochesalna.authorizationservice.com.yazikochesalna.authorizationservice.dto.GetLoginResponseDto
import com.yazikochesalna.authorizationservice.com.yazikochesalna.authorizationservice.dto.RegisterRequestDto
import com.yazikochesalna.authorizationservice.service.AuthService
import com.yazikochesalna.authorizationservice.exception.AuthException
import com.yazikochesalna.authorizationservice.dto.AuthRequestDto
import com.yazikochesalna.authorizationservice.dto.TokenResponseDto
import com.yazikochesalna.authorizationservice.entity.UserAuth
import com.yazikochesalna.common.authentication.JwtAuthenticationToken
import com.yazikochesalna.common.service.JwtService
import io.swagger.v3.oas.annotations.Hidden
import jakarta.annotation.security.RolesAllowed
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val autService: AuthService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequestDto): ResponseEntity<TokenResponseDto> {
        if (autService.isUserRegistered(request.login)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        val user = autService.createUser(
            login = request.login,
            username = request.username,
            password =  passwordEncoder.encode(request.password)
        )

        val token = jwtService.generateAccessToken(user.id)
        return ResponseEntity.ok(TokenResponseDto(token))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequestDto): ResponseEntity<TokenResponseDto> {
        val userAuth: UserAuth
        try {
            userAuth = autService.loadUserByLogin(request.login)
        } catch (exeption: UsernameNotFoundException){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        if (!passwordEncoder.matches(request.password, userAuth.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
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

    @GetMapping("/login")
    @RolesAllowed("SERVICE")
    @Hidden
    fun getLogin(userID: Long): ResponseEntity<GetLoginResponseDto> {
        val login: String? = autService.getUserLogin(userID);
        if (login != null) {
            return ResponseEntity.ok(GetLoginResponseDto(login))
        }
        return ResponseEntity.notFound().build()
    }
}

