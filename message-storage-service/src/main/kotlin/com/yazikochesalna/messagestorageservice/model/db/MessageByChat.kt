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
    var chatId: Long,

    @PrimaryKeyColumn(name = "send_time", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    var sendTime: LocalDateTime,

    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.CLUSTERED, ordinal = 2)
    var id: UUID,

    @Column("text")
    var text: String?,

    @Column("sender_id")
    var senderId: Long,

    @Column("type")
    @Enumerated(value = EnumType.STRING)
    var type: MessageType,

    var markedToDelete: Boolean = false)