package com.yazikochesalna.authorizationservice.exception.handler

import com.yazikochesalna.authorizationservice.exception.AuthException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class AuthExceptionHandler {

    @ExceptionHandler(AuthException::class, AuthenticationException::class)
    fun handleAuthException(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(e.message ?: "Authentication failed"))
    }
    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUserValidationException(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(e.message ?: "User not found"))
    }
}

data class ErrorResponse(val message: String)