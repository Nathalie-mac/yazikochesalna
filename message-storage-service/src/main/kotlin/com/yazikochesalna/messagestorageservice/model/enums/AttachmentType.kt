package com.yazikochesalna.messagestorageservice.model.enums

import com.yazikochesalna.messagestorageservice.exception.customexceptions.ErrorInEnumException

enum class AttachmentType(val at: String) {
    REPLY("REPLY"),
    FILE("FILE"),
    PIN("PIN"),
    NEW_CHAT_AVATAR("NEW_CHAT_AVATAR");


    companion object {

        fun fromType(at: String?): AttachmentType {
            if (at == null) {
                throw ErrorInEnumException("Attachment type cannot be null!")
            }
            return entries.find { it.at == at }
                ?: throw ErrorInEnumException("Unknown AttachmentType: $at")
        }
    }
}