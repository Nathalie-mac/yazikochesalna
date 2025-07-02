package com.yazikochesalna.messagestorageservice.repository

import com.datastax.oss.driver.api.core.cql.SimpleStatement
import com.yazikochesalna.messagestorageservice.model.db.Message
import jakarta.annotation.PostConstruct
import org.springframework.data.cassandra.core.ReactiveCassandraOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
class CustomMessageRepositoryImpl(
    private val operations: ReactiveCassandraOperations
) : CustomMessageRepository {

    private lateinit var cursorMessagesSelectStmt: String

    //TODO: дописать запросик
    @PostConstruct
    fun init() {
        cursorMessagesSelectStmt = """
            SELECT * FROM messages 
            WHERE chat_id = ? 
            AND sender_id IN ? 
            LIMIT 20
        """.trimIndent()
    }

    //жестко достаем сообщения
    override fun findMessagesByCursor(
        userId: Long,
        chatId: Long,
        cursor: UUID,
        limitUp: Int,
        limitDown: Int
    ): Flux<Message> {
        val stmt = SimpleStatement.newInstance(
            cursorMessagesSelectStmt,
            userId, chatId, cursor, limitUp, limitDown
        )
        return operations.select(stmt, Message::class.java)
    }
}