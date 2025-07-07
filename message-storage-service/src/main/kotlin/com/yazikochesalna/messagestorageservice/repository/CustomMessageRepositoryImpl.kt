package com.yazikochesalna.messagestorageservice.repository

import com.datastax.oss.driver.api.core.cql.SimpleStatement
import com.yazikochesalna.messagestorageservice.model.MessageType
import com.yazikochesalna.messagestorageservice.model.db.Message
import jakarta.annotation.PostConstruct
import org.springframework.data.cassandra.core.ReactiveCassandraOperations
import org.springframework.data.cassandra.core.cql.ReactiveCqlTemplate
import org.springframework.data.cassandra.core.cql.queryForFlux
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.util.*

@Repository
open class CustomMessageRepositoryImpl(
    private val reactiveCqlTemplate: ReactiveCqlTemplate
) : CustomMessageRepository {

    private lateinit var cursorMessagesSelectStmt: String

    //TODO: дописать запросик
    @PostConstruct
    fun init() {
        cursorMessagesSelectStmt = """
            SELECT * FROM messages 
            WHERE chat_id = 1 
            LIMIT 20
        """.trimIndent()
    }

    //жестко достаем сообщения
    override fun findMessagesByCursor(
        chatId: Long,
        cursor: UUID,
        limitUp: Int,
        limitDown: Int
    ): Flux<Message> {
        return reactiveCqlTemplate.query("select * from messages", {
            row, _ ->
            row.getUuid("id")?.let {
                row.get("send_time", LocalDateTime::class.java)?.let { it1 ->
                    Message(id = it,
                        type = MessageType.fromType(row.getString("type")),
                        chatId = row.getLong("chat_id"),
                        senderId = row.getLong("sender_id"),
                        sendTime = it1,
                        text = row.getString("text"),
                        markedToDelete = row.getBoolean("marked_to_delete"))
                }
            }
        })
    }
}