package com.yazikochesalna.messagestorageservice.model.enums

import com.yazikochesalna.messagestorageservice.exception.customexceptions.ErrorInEnumException

enum class MessageType(val type: String) {
    MESSAGE("MESSAGE"),
    NEW_MEMBER("NEW_MEMBER"),
    DROP_MEMBER("DROP_MEMBER"),
    PIN("PIN"),
    NEW_CHAT_AVATAR("NEW_CHAT_AVATAR");

    companion object {
        fun fromType(type: String?): MessageType {
            if (type == null) {
                throw ErrorInEnumException("Message type cannot be null!")
            }
            return entries.find { it.type == type }
                ?:throw ErrorInEnumException("Unknown MessageType: $type")
        }
    }
}