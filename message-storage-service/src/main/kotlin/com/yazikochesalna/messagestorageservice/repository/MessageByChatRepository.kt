package com.yazikochesalna.messagestorageservice.repository

import com.yazikochesalna.messagestorageservice.model.db.MessageByChat
import com.yazikochesalna.messagestorageservice.model.db.Message
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface MessageByChatRepository: ReactiveCassandraRepository<MessageByChat?, UUID?> {

    fun saveAll(message: List<MessageByChat>): Flux<MessageByChat>

    fun findFirstByChatId(chatId: Long): Mono<MessageByChat?>
}