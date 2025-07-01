package com.yazikochesalna.messagestorageservice.model

import org.springframework.data.cassandra.core.mapping.*
import java.time.LocalDateTime
import java.util.UUID


@Table("message")
data class Message @JvmOverloads constructor(
    @PrimaryKey
    val id: UUID,

    @Column("sender_id")
    @Indexed
    val senderId: Long,

    @Column("chat_id")
    @Indexed
    val chatId: Long,

    @Column
    val text: String,

    @Column("send_time")

    val sendTime: LocalDateTime,

    @Column("marked_to_delete")
    var markedToDelete: Boolean = false
)