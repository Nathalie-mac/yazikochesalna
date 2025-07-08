package com.yazikochesalna.messagestorageservice.model.enums

enum class MessageType(val type: String) {
    MESSAGE("MESSAGE"),
    NEW_MEMBER("NEW_MEMBER"),
    DROP_MEMBER("DROP_MEMBER"),
    PIN("PIN"),
    NEW_AVATAR("NEW_AVATAR");

    companion object {
        fun fromType(type: String?): MessageType? {
            return type?.let { notNullType ->
                entries.firstOrNull { it.type == notNullType }
            }
        }
    }
}