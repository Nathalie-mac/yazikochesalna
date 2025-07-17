package com.yazikochesalna.messagestorageservice.dto.payloads

import com.fasterxml.jackson.annotation.JsonProperty
import com.yazikochesalna.messagestorageservice.model.enums.AttachmentType
import lombok.NoArgsConstructor
import java.util.UUID

@NoArgsConstructor
data class MessageAttachmentDTO(
    @JsonProperty("type")
    var type: AttachmentType,
    @JsonProperty("id")
    var id: UUID
)
