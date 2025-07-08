package com.yazikochesalna.messagestorageservice.model.enums

import com.yazikochesalna.messagestorageservice.exception.ErrorInMessageTypeException

enum class MessageType(val type: String) {
    MESSAGE("MESSAGE"),
    NEW_MEMBER("NEW_MEMBER"),
    DROP_MEMBER("DROP_MEMBER"),
    PIN("PIN"),
    NEW_AVATAR("NEW_AVATAR");

    companion object {
        fun fromType(type: String?): MessageType {
            return type?.let { type ->
                entries.firstOrNull { it.type == type }
                    ?: throw ErrorInMessageTypeException("Unknown MessageType: $type")
            } ?: throw ErrorInMessageTypeException("Message type cannot be null!")
        }
    }
}