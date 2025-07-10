package com.yazikochesalna.messagestorageservice.model.db

import com.yazikochesalna.messagestorageservice.model.enums.AttachmentType
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
    var id: Long,

    @Column("message_id")
    @Indexed
    var message_id: UUID,

    @Column("attachment_type")
    @Enumerated(value = EnumType.STRING)
    var attachmentType: AttachmentType,

    @Column("attachment")
    var attachment: String
)