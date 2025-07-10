package com.yazikochesalna.authorizationservice.service

import com.yazikochesalna.authorizationservice.dto.TokenResponseDto
import com.yazikochesalna.authorizationservice.entity.UserAuth
import com.yazikochesalna.authorizationservice.exception.AuthException
import com.yazikochesalna.authorizationservice.exception.UserNotFoundException
import com.yazikochesalna.authorizationservice.repository.UserRepository
import com.yazikochesalna.common.service.JwtService
import lombok.RequiredArgsConstructor
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
open class AuthService(
    private val userServiceClient: UserServiceClient,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {

    fun isUserRegistered(login: String): Boolean = userRepository.existsByLogin(login)

    fun registerUser(login: String, username: String, password: String): TokenResponseDto =
        generateTokens(createUser(login, username, password = passwordEncoder.encode(password)).id)


    fun loginUser(login: String, password: String): TokenResponseDto {
        val user: UserAuth = runCatching {
            userRepository.findByLogin(login)
        }.getOrElse {
            throw AuthException("Invalid credentials")
        } ?: throw AuthException("Invalid credentials")

        if (!passwordEncoder.matches(password, user.password)) {
            throw AuthException("Invalid credentials")
        }

        return generateTokens(user.id)
    }

    fun getUserLogin(userId: Long): String =
        userRepository.findById(userId).getOrNull()?.login ?: throw UserNotFoundException("User id not found: $userId")

    fun refreshTokens(userId: Long) = generateTokens(userId)

    private fun createUser(login: String, username: String, password: String): UserAuth {
        val userId = userServiceClient.saveUser(username)
        val userAuth = UserAuth(id = userId, login = login, password = password)
        return userRepository.save(userAuth)
    }

    private fun generateTokens(userId: Long): TokenResponseDto = TokenResponseDto(
        accessToken = jwtService.generateAccessToken(userId),
        refreshToken = jwtService.generateRefreshToken(userId)
    )

}