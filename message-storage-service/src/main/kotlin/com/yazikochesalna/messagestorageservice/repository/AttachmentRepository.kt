package com.yazikochesalna.messagestorageservice.repository

import com.yazikochesalna.messagestorageservice.model.db.Attachment
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface AttachmentRepository: ReactiveCassandraRepository<Attachment?, Long?> {
    fun saveAll(attachments: List<Attachment>): Flux<Attachment>

    fun findByMessageIdIn(messageIds: List<UUID>): Flux<Attachment>

}