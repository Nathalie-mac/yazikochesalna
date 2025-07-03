package com.yazikochesalna.authorizationservice.repository

import com.yazikochesalna.authorizationservice.entity.UserAuth
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserAuth, Long> {
    fun findByLogin(username: String): UserAuth?
    fun existsByLogin(username: String): Boolean
}