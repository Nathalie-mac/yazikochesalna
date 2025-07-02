package com.yazikochesalna.messagestorageservice.model.db

import com.yazikochesalna.messagestorageservice.model.AttachmentType
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.Indexed
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.util.*

@Table("attachments")
data class Attachment(
    @PrimaryKey
    val id: Long,

    @Column("message_id")
    @Indexed
    val message_id: UUID,

    @Column("attachment_type")
    @Enumerated(value = EnumType.STRING)
    val attachmentType: AttachmentType,

    @Column("attachment")
    val attachment: String
)