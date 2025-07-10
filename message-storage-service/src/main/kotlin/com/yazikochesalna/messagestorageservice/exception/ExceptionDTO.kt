package com.yazikochesalna.messagestorageservice.exception

data class ExceptionDTO(
    val message: String,
    val details: Map<String, Any>
)
