package com.yazikochesalna.messagestorageservice.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import com.yazikochesalna.messagestorageservice.dto.serializers.MessageJsonFormatDeserializer
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import java.time.LocalDateTime
import java.util.*



@JsonDeserialize(using = MessageJsonFormatDeserializer::class)
@Builder
@Data
class MessagesJsonFormatDTO(
    var messageId: UUID,
    var type: MessageType,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    var timestamp: LocalDateTime,
    var payload: PayLoadDTO?
) {
//    fun <T : PayLoadDTO?> getPaLoad(): T {
//        return payload as T
//    }
}