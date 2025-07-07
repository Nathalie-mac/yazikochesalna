package com.yazikochesalna.authorizationservice.exception.userservice

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class UserConflictCustomException(cause: String) : RuntimeException(cause) {
}