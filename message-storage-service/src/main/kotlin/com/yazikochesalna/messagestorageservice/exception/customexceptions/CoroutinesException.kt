package com.yazikochesalna.messagestorageservice.exception.customexceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
class CoroutinesException(message: String, cause: Throwable? = null): RuntimeException(message, cause)