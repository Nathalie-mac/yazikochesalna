package com.yazikochesalna.authorizationservice.exception.userservice

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
class UserCreationCustomException(cause: Throwable?): RuntimeException(cause)

