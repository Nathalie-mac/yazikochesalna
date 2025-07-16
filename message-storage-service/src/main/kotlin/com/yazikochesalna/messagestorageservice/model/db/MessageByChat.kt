package com.yazikochesalna.messagestorageservice.model.db

import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.time.LocalDateTime
import java.util.*


@Table("messages_by_chat")
data class MessageByChat (
    @PrimaryKeyColumn(name = "chat_id", type = PrimaryKeyType.PARTITIONED)
    override val chatId: Long,

    @PrimaryKeyColumn(name = "send_time", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    override val sendTime: LocalDateTime,

    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.CLUSTERED, ordinal = 2)
    override val id: UUID,

    @Column("text")
    override val text: String?,

    @Column("sender_id")
    override val senderId: Long,

    @Column("type")
    @Enumerated(value = EnumType.STRING)
    override val type: MessageType,

    override val markedToDelete: Boolean = false
):BaseMessage