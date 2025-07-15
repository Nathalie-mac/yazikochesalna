package com.yazikochesalna.messagestorageservice.repository

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveSession
import com.datastax.oss.driver.api.core.cql.Row
import com.yazikochesalna.messagestorageservice.exception.customexceptions.NullCassandraFiledException
import com.yazikochesalna.messagestorageservice.model.db.Message
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.cassandra.core.cql.ReactiveCqlTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.IllegalStateException
import java.time.LocalDateTime
import java.util.*


@Repository
open class CustomMessageRepositoryImpl(
    private val reactiveCqlTemplate: ReactiveCqlTemplate,
    @Qualifier("session") private val reactiveSession: ReactiveSession
) : CustomMessageRepository {

    private lateinit var cursorMessageSelectStmt: String
    private lateinit var beforeCursorSelectStmt: String
    private lateinit var afterCursorSelectStmt: String

    @PostConstruct
    fun init() {
        beforeCursorSelectStmt = CassandraQueries.BEFORE_CURSOR_SELECT.trimIndent()

        afterCursorSelectStmt = CassandraQueries.AFTER_CURSOR_SELECT.trimIndent()

        cursorMessageSelectStmt = CassandraQueries.CURSOR_MESSAGE_SELECT.trimIndent()
    }

    override fun findMessagesByCursor(
        chatId: Long,
        cursor: UUID,
        limitUp: Int,
        limitDown: Int
    ): Flux<Message> {
        val cursorMessageMono = reactiveCqlTemplate.query(
            cursorMessageSelectStmt,
            { rs, _ -> deserializeMessage(rs) },
            cursor).single()

        return cursorMessageMono.flatMapMany { cursorMessage ->
            requireNotNull(cursorMessage) { "Cursor message not found" }
            val cursorTime = cursorMessage.sendTime

            val beforeMessagesFlux = if (limitUp > 0) {
                reactiveCqlTemplate.query(
                    beforeCursorSelectStmt,
                    { rs, _ -> deserializeMessage(rs) },
                    chatId, cursorTime, limitUp
                ).collectList()
            } else {
                Mono.just(emptyList<Message>())
            }

            val afterMessagesFlux = if (limitDown > 0) {
                reactiveCqlTemplate.query(
                    afterCursorSelectStmt,
                    { rs, _ -> deserializeMessage(rs) },
                    chatId, cursorTime, limitDown
                ).collectList()
            } else {
                Mono.just(emptyList<Message>())
            }

            Flux.zip(
                beforeMessagesFlux,
                afterMessagesFlux
            ).map { tuple ->
                val finalList = mutableListOf<Message>()
                val beforeMessages = tuple.t1.filterNotNull()
                val afterMessages  = tuple.t2.filterNotNull()


                finalList.addAll(beforeMessages.reversed())
                finalList.add(cursorMessage)
                finalList.addAll(afterMessages)

                finalList
            }.flatMapIterable { it }
        }
    }


    private fun deserializeMessage(rs: Row): Message = kotlin.runCatching {
        Message(
            id = rs.getUuid("id") ?: throw NullCassandraFiledException("id"),
            type = MessageType.fromType(rs.getString("type")) ?: throw NullCassandraFiledException("type"),
            senderId = rs.getLong("sender_id"),
            chatId = rs.getLong("chat_id"),
            text = rs.getString("text"),
            sendTime = rs.get("send_time", LocalDateTime::class.java) ?: throw NullCassandraFiledException("send_time"),
            markedToDelete = rs.getBoolean("marked_to_delete")
        )
    }.getOrElse { e ->
        when (e) {
            is NullCassandraFiledException -> throw e
            else -> throw IllegalStateException("Deserialization in table Message failed", e)
        }
    }
}