package com.yazikochesalna.messagestorageservice.repository

import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import com.yazikochesalna.messagestorageservice.model.MessageType
import com.yazikochesalna.messagestorageservice.model.db.Message
import jakarta.annotation.PostConstruct
import org.springframework.data.cassandra.core.ReactiveCassandraOperations
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate
import org.springframework.data.cassandra.core.cql.ReactiveCqlTemplate
import org.springframework.data.cassandra.core.cql.queryForFlux
import org.springframework.data.cassandra.core.cql.queryForObject
import org.springframework.data.cassandra.core.query.Criteria
import org.springframework.data.cassandra.core.query.Query
import org.springframework.data.cassandra.core.query.and
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*

@Repository
open class CustomMessageRepositoryImpl(
    private val reactiveCqlTemplate: ReactiveCqlTemplate,
    private val reactiveCassandraTemplate: ReactiveCassandraTemplate
) : CustomMessageRepository {

    private lateinit var cursorMessageSelectStmt: String
    private lateinit var beforeCursorSelectStmt: String
    private lateinit var afterCursorSelectStmt: String

    @PostConstruct
    fun init() {
        beforeCursorSelectStmt = """
            SELECT * FROM messages_by_chat 
            WHERE chat_id = ? 
            AND send_time < ?
            ORDER BY send_time DESC
            LIMIT ?
        """.trimIndent()

        afterCursorSelectStmt = """
            SELECT * FROM messages_by_chat 
            WHERE chat_id = ? 
            AND send_time > ?
            ORDER BY send_time ASC
            LIMIT ?
        """.trimIndent()

        cursorMessageSelectStmt = """
            SELECT * FROM messages 
            WHERE id = ?
        """.trimIndent()
    }

    override fun findMessagesByCursor(
        chatId: Long,
        cursor: UUID,
        limitUp: Int,
        limitDown: Int
    ): Flux<Message> {
        val cursorMessageMono = reactiveCqlTemplate.query(
            "SELECT * FROM messages WHERE id = ?",
            { rs, _ -> deserializeMessage(rs) },
            cursor
        ).single()

        return cursorMessageMono.flatMapMany { cursorMessage ->
            requireNotNull(cursorMessage) { "Cursor message not found" }
            val cursorTime = cursorMessage.sendTime

            val beforeMessagesFlux = reactiveCqlTemplate.query(
                beforeCursorSelectStmt,
                { rs, _ -> deserializeMessage(rs) },
                chatId, cursorTime, limitUp
            ).collectList()

            val afterMessagesFlux = reactiveCqlTemplate.query(
                afterCursorSelectStmt,
                { rs, _ -> deserializeMessage(rs) },
                chatId, cursorTime, limitDown
            ).collectList()

            Flux.zip(
                beforeMessagesFlux,
                afterMessagesFlux
            ).map { tuple ->
                val finalList = mutableListOf<Message>()
                val beforeMessages = tuple.t1.filterNotNull()
                finalList.addAll(beforeMessages.reversed())
                if (cursorMessage != null) {
                    finalList.add(cursorMessage)
                }
                val afterMessages  = tuple.t2.filterNotNull()
                finalList.addAll(afterMessages)
                finalList
            }.flatMapIterable { it }
        }
    }

    //TODO: сериализация с помощью MessageCodec
    private fun deserializeMessage(rs: Row): Message? {
        return rs.getUuid("id")?.let {
            rs.get("send_time", LocalDateTime::class.java)?.let { it1 ->
                rs.getString("text")?.let { it2 ->
                    Message(
                        id = it,
                        type = MessageType.fromType(rs.getString("type")),
                        senderId = rs.getLong("sender_id"),
                        chatId = rs.getLong("chat_id"),
                        text = it2,
                        sendTime = it1,
                        markedToDelete = rs.getBoolean("marked_to_delete")
                    )
                }
            }
        }
    }
}