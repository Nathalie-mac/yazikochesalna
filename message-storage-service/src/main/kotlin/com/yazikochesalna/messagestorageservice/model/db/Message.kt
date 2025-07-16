package com.yazikochesalna.messagestorageservice.model.db

import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.LocalDateTime
import java.util.*


@Table("messages")
data class Message(
    @PrimaryKey
    var id: UUID,

    @Column("type")
    @Enumerated(value = EnumType.STRING)
    var type: MessageType,

    @Column("sender_id")
    var senderId: Long,

    @Column("chat_id")
    var chatId: Long,

    @Column
    var text: String?,

    @Column("send_time")
    val sendTime: LocalDateTime,

    @Column("marked_to_delete")
    var markedToDelete: Boolean = false
)