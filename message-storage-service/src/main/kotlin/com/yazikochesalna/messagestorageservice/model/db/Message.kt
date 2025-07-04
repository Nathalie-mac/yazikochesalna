package com.yazikochesalna.messagestorageservice.model.db

import com.yazikochesalna.messagestorageservice.model.MessageType
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.Indexed
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.LocalDateTime
import java.util.*


@Table("messages")
data class Message(
    @PrimaryKey
    val id: UUID,

    @Column("type")
    @Enumerated(value = EnumType.STRING)
    val type: MessageType,

    @Column("sender_id")
    @Indexed
    val senderId: Long,

    @Column("chat_id")
    @Indexed
    val chatId: Long,

    @Column
    val text: String?,

    @Column("send_time")
    @Indexed
    val sendTime: LocalDateTime,

    @Column("marked_to_delete")
    var markedToDelete: Boolean = false
)