package com.yazikochesalna.authorizationservice.service

import com.yazikochesalna.authorizationservice.entity.UserAuth
import com.yazikochesalna.authorizationservice.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class UserService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByLogin(username)
            ?: throw UsernameNotFoundException("User not found: $username")
    }

    fun isUserRegistered(username: String): Boolean = userRepository.existsByLogin(username)

    fun createUser(username: String, password: String): UserAuth {
        val userAuth = UserAuth(login = username, password = password)
        return userRepository.save(userAuth)
    }

    fun loadUserByLoginAndPassword(username: String, password: String): UserDetails {
        return userRepository.findByLoginAndPassword(username, password)
            ?: throw UsernameNotFoundException("User not found: $username")
    }
}