package com.yazikochesalna.messagestorageservice.model

enum class AttachmentType(val at: String){
    REPLY("reply"),
    FORWARD("forward"),
    PIN("pin"),
    ATTACHMENT("attachment");
    companion object {
        fun fromType(at: String): AttachmentType? {
            return AttachmentType.values().firstOrNull { it.at == at }
        }
    }
}