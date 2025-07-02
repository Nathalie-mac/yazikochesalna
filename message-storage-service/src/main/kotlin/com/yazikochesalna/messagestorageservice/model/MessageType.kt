package com.yazikochesalna.messagestorageservice.model

enum class MessageType(val type: String) {
    MESSAGE("message"),
    NEW_MEMBER("new_member"),
    DROP_MEMBER("drop_member"),
    PIN("pin"),
    NEW_AVATAR("new_avatar");

    companion object {
        fun fromType(type: String): MessageType? {
            return values().firstOrNull { it.type == type }
        }
    }
}