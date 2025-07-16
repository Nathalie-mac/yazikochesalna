package com.yazikochesalna.messagestorageservice.model.enums

import com.yazikochesalna.messagestorageservice.exception.customexceptions.ErrorInEnumException

enum class AttachmentType(val at: String) {
    REPLY("REPLY"),
    FORWARD("FORWARD"),
    PIN("PIN"),
    ATTACHMENT("ATTACHMENT"),
    NEW_AVATAR("NEW_AVATAR");

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