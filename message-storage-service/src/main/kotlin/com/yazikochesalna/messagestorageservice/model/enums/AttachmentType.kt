package com.yazikochesalna.messagestorageservice.model.enums

import com.yazikochesalna.messagestorageservice.exception.customexceptions.ErrorInEnumException

enum class AttachmentType(val at: String){
    REPLY("REPLY"),
    FORWARD("FORWARD"),
    PIN("PIN"),
    ATTACHMENT("ATTACHMENT");
    companion object {
        fun fromType(at: String): AttachmentType? {
            return at?.let{type ->
                entries.firstOrNull{it.at == type}
                    ?: throw ErrorInEnumException("Unknown AttachmentType: $type")
            }
        }
    }
}