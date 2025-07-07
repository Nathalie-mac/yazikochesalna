package com.yazikochesalna.authorizationservice.service

interface UserServiceClient {
    abstract fun saveUser(username: String): Long
}