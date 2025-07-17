package com.yazikochesalna.messagestorageservice.exception

data class ExceptionDTO(
    val problem: String,
    val details: Map<String, Any>? = null
)
