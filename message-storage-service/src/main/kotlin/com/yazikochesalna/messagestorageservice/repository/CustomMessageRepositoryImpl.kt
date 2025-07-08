package com.yazikochesalna.messagestorageservice.repository

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveSession
import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder
import com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal
import com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom
import com.yazikochesalna.messagestorageservice.exception.NullCassandraFiledException
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
        beforeCursorSelectStmt = """  
            SELECT * FROM messages_by_chat            WHERE chat_id = ?   
            AND send_time < ?  
            ORDER BY send_time DESC            LIMIT ?        """.trimIndent()

        afterCursorSelectStmt = """  
            SELECT * FROM messages_by_chat            WHERE chat_id = ?   
            AND send_time > ?  
            ORDER BY send_time ASC            LIMIT ?        """.trimIndent()

        cursorMessageSelectStmt = """  
            SELECT * FROM messages            WHERE id = ?  
        """.trimIndent()
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
                val afterMessages  = tuple.t2.filterNotNull()


                finalList.addAll(beforeMessages.reversed())
                finalList.add(cursorMessage)
                finalList.addAll(afterMessages)

                finalList
            }.flatMapIterable { it }
        }//
//        val cursorFlx = Flux
//            .from(reactiveSession.executeReactive(cursorQuery))
//            .map { row -> deserializeMessage(row) }
//            //.switchIfEmpty(Mono.error(("Cursor message not found")))
//
//        return cursorFlx.flatMap { cursorMessage ->
//            val cursorTime = cursorMessage?.sendTime
//
//            val beforeCursorQuery = selectFrom("messages_by_chat")
//                .all().whereColumn("chat_id").isEqualTo(literal(chatId))
//                .whereColumn("send_time").isLessThan(literal(cursorTime))
//                .orderBy("send_time", ClusteringOrder.DESC)
//                .limit(limitUp).build()
//
//            val afterCursorQuery = selectFrom("messages_by_chat")
//                .all().whereColumn("chat_id").isEqualTo(literal(chatId))
//                .whereColumn("send_time").isGreaterThan(literal(cursorTime))
//                .orderBy("send_time", ClusteringOrder.ASC)
//                .limit(limitDown).build()
//
//            val beforeMessagesFlux = Flux.from(reactiveSession.executeReactive(afterCursorQuery))
//                .map { row -> deserializeMessage(row) }
//
//            val afterMessagesFlux = Flux.from(reactiveSession.executeReactive(beforeCursorQuery))
//                .map { row -> deserializeMessage(row) }
//
//            Flux.concat(
//                beforeMessagesFlux.collectList().map { it.reversed() },
//                Flux.just(cursorMessage),
//                afterMessagesFlux.collectList()
//            )
////            Flux.zip(beforeMessagesFlux, afterMessagesFlux)
////                .flatMapIterable { (beforeMessages, afterMessages) ->
////                    val result = mutableListOf<Message>()
////                    result.addAll(beforeMessages.reversed()) // Старые сообщения (DESC → переворачиваем)
////                    result.add(cursorMessage) // Сам курсор
////                    result.addAll(afterMessages) // Новые сообщения (ASC)
////                    result
////                }
    }


    private fun deserializeMessage(rs: Row): Message = kotlin.runCatching {
        Message(
            id = rs.getUuid("id") ?: throw NullCassandraFiledException("id"),
            type = MessageType.fromType(rs.getString("type")) ?: throw NullCassandraFiledException("type"),
            senderId = rs.getLong("sender_id"),
            chatId = rs.getLong("chat_id"),
            text = rs.getString("text"),
            sendTime = LocalDateTime.now(),//rs.get("send_time", LocalDateTime::class.java)
            //?: throw NullCassandraFiledException("send_time"),            markedToDelete = rs.getBoolean("marked_to_delete")
        )
    }.getOrElse { e ->
        when (e) {
            is NullCassandraFiledException -> throw e
            else -> throw IllegalStateException("Deserialization in table Message failed", e)
        }
    }
}