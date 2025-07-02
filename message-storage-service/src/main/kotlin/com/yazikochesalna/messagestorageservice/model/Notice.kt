package com.yazikochesalna.messagestorageservice.model

import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.Indexed
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

class Notice constructor(
    val id: UUID,

    val senderId: Long,

    val chatId: Long,

    val sendTime: LocalDateTime
)