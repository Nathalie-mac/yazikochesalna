package com.yazikochesalna.authorizationservice.service

import com.yazikochesalna.authorizationservice.com.yazikochesalna.authorizationservice.controller.UserServiceClient
import com.yazikochesalna.authorizationservice.entity.UserAuth
import com.yazikochesalna.authorizationservice.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class AuthService(
    private val userServiceClient: UserServiceClient,
    private val userRepository: UserRepository
) {

    fun loadUserByLogin(login: String): UserAuth {
        return userRepository.findByLogin(login)
            ?: throw UsernameNotFoundException("User not found: $login")
    }

    fun isUserRegistered(login: String): Boolean = userRepository.existsByLogin(login)

    fun createUser(login: String, username: String, password: String): UserAuth {
        val userId = userServiceClient.saveUser(username)
        val userAuth = UserAuth(id = userId, login = login, password = password)
        return userRepository.save(userAuth)
    }

}