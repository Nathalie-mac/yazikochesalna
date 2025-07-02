package com.yazikochesalna.messagestorageservice.repository

import com.yazikochesalna.messagestorageservice.model.db.Message
import org.springframework.data.cassandra.repository.AllowFiltering
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
interface MessageRepository : ReactiveCassandraRepository<Message?, UUID?>, CustomMessageRepository {

    //пишем сообщения
    fun saveAll(messages: Flux<Message>): Flux<Message>

    //достаем сообщения
    //fun findMessagesByCursor(userId: Long, chatId: Long, cursor: UUID, limitUp: Int, limitDown: Int): Flux<Message>
}