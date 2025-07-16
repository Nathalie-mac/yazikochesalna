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
    override val id: UUID,

    @Column("type")
    @Enumerated(value = EnumType.STRING)
    override val type: MessageType,

    @Column("sender_id")
    override val senderId: Long,

    @Column("chat_id")
    override val chatId: Long,

    @Column
    override val text: String?,

    @Column("send_time")
    override val sendTime: LocalDateTime,

    @Column("marked_to_delete")
    override val markedToDelete: Boolean = false
): BaseMessage