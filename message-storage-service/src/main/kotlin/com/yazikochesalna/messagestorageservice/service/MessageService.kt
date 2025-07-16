package com.yazikochesalna.messagestorageservice.service

import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.NewestMessageDTO
import com.yazikochesalna.messagestorageservice.model.db.Attachment
import com.yazikochesalna.messagestorageservice.model.db.convertor.CassandraEntitiesConvertor
import com.yazikochesalna.messagestorageservice.model.db.Message
import com.yazikochesalna.messagestorageservice.model.db.MessageByChat
import com.yazikochesalna.messagestorageservice.repository.AttachmentRepository
import com.yazikochesalna.messagestorageservice.repository.MessageByChatRepository
import com.yazikochesalna.messagestorageservice.repository.MessageRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

private const val BLOCK_DURATION: Long = 20
private const val BATCH_SIZE: Int = 20

@Service
open class MessageService(
    private val chatServiceClient: ChatServiceClient,
    private val messageRepository: MessageRepository,
    private val messageByChatRepository: MessageByChatRepository,
    private val attachmentRepository: AttachmentRepository,
    private val cassandraEntitiesConvertor: CassandraEntitiesConvertor,
) {

    private fun isChatMember(userId: Long, chatId: Long): Boolean =
        chatServiceClient.checkUserInChat(userId, chatId)

    fun getMessagesAroundCursor(
        userId: Long,
        chatId: Long,
        cursor: UUID?,
        limitUp: Int,
        limitDown: Int
    ): List<MessagesJsonFormatDTO> {
        if (!isChatMember(userId, chatId)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        return getCassandraMessages(chatId, cursor, limitUp, limitDown)
    }

    private fun getCassandraMessages(
        chatId: Long,
        cursor: UUID?,
        limitUp: Int,
        limitDown: Int
    ): List<MessagesJsonFormatDTO> {
        val cassandraMessages: Flux<Message> = when (cursor) {
            null -> messageRepository.findMessagesWithoutCursor(chatId, limitUp)
            else -> messageRepository.findMessagesByCursor(chatId, cursor, limitUp, limitDown)
        }
        return cassandraMessages
            .map { message -> cassandraEntitiesConvertor.convertToMessagesJsonFormatDto(message) }
            .collectList()
            .block(Duration.ofSeconds(BLOCK_DURATION)) ?: emptyList()
    }

    fun saveMessagesBatch(messages: List<MessagesJsonFormatDTO>): Mono<Void> {
        if (messages.isEmpty() || messages.size > BATCH_SIZE) {
            return Mono.error(IllegalArgumentException("Invalid batch size"))
        }

        val messagePairs = messages.map { cassandraEntitiesConvertor.convertToMessageWithAttachments(it) }
        val messages = messagePairs.map { it.first }
        val messagesByChat = messagePairs.map { cassandraEntitiesConvertor.convertToMessageByChat(it.first) }
        val attachments = messagePairs.flatMap { it.second }

        return saveMesagesToAllTables(messages, messagesByChat, attachments).then()
    }

    private fun saveMesagesToAllTables(messages: List<Message>, messagesByChat: List<MessageByChat>, attachments: List<Attachment>): Mono<Void> {
        return Mono.zip(
            messageRepository.saveAll(messages).collectList(),
            messageByChatRepository.saveAll(messagesByChat).collectList(),
            if (attachments.isNotEmpty()) {
                attachmentRepository.saveAll(attachments).collectList()
            } else {
                Mono.just(emptyList())
            }
        ).then()
    }


    fun getNewestMessagesByChat(chatList: List<Long>): List<NewestMessageDTO> {
        return chatList.map { chatId ->
            val lastMessage = messageByChatRepository.findFirstByChatId(chatId).block()
            NewestMessageDTO(
                chatId = chatId,
                lastMessage = lastMessage?.let { cassandraEntitiesConvertor.convertToMessagesJsonFormatDto(it) }
            )
        }
    }
}