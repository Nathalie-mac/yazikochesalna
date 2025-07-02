package com.yazikochesalna.messagestorageservice.repository

import com.yazikochesalna.messagestorageservice.model.db.Attachment
import com.yazikochesalna.messagestorageservice.model.db.Message
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AttachmentRepository: ReactiveCassandraRepository<Attachment?, Long?> {

}