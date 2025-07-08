package com.yazikochesalna.messagestorageservice.model.enums

import com.yazikochesalna.messagestorageservice.exception.ErrorInAttachmentTypeException
import com.yazikochesalna.messagestorageservice.exception.ErrorInMessageTypeException

enum class AttachmentType(val at: String){
    REPLY("REPLY"),
    FORWARD("FORWARD"),
    PIN("PIN"),
    ATTACHMENT("ATTACHMENT");
    companion object {
        fun fromType(at: String): AttachmentType? {
            return at?.let{type ->
                entries.firstOrNull{it.at == type}
                    ?: throw ErrorInAttachmentTypeException("Unknown AttachmentType: $type")
            }?: throw ErrorInAttachmentTypeException("Attachment type cannot be null!")
        }
    }
}