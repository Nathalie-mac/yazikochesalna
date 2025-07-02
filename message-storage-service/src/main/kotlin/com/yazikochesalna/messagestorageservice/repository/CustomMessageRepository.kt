package com.yazikochesalna.messagestorageservice.repository

import com.yazikochesalna.messagestorageservice.model.db.Message
import reactor.core.publisher.Flux
import java.util.*

interface CustomMessageRepository {
    fun findMessagesByCursor(userId: Long, chatId: Long, cursor: UUID, limitUp: Int, limitDown: Int): Flux<Message>

    //fun saveMessagesBatch(messages: List<Message>): Flux<Message>

}