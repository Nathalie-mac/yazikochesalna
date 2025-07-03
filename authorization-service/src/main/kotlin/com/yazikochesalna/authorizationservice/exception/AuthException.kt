package com.yazikochesalna.authorizationservice.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
class AuthException(message: String) : RuntimeException(message)