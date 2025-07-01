package com.yazikochesalna.messagestorageservice.repository

import com.yazikochesalna.messagestorageservice.model.Message
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository
import java.util.UUID

//пока синхронный
@Repository
interface MessageRepository: CassandraRepository<Message, UUID> {

}