package com.yazikochesalna.messagestorageservice.model.enums

enum class AttachmentType(val at: String){
    REPLY("REPLY"),
    FORWARD("FORWARD"),
    PIN("PIN"),
    ATTACHMENT("ATTACHMENT");
    companion object {
        fun fromType(at: String): AttachmentType? {
            return AttachmentType.values().firstOrNull { it.at == at }
        }
    }
}