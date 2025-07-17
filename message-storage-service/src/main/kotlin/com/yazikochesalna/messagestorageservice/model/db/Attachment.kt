package com.yazikochesalna.messagestorageservice.model.db

import com.yazikochesalna.messagestorageservice.model.enums.AttachmentType
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.*
import java.util.*

@Table("attachments")
data class Attachment(
    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    var id: UUID,

    @PrimaryKeyColumn(name = "message_id", type = PrimaryKeyType.PARTITIONED)
    var messageId: UUID,

    @Column("type")
    @Enumerated(value = EnumType.STRING)
    var type: AttachmentType,

)