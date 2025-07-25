package com.yazikochesalna.messagestorageservice.service

import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.NewestMessageDTO
import com.yazikochesalna.messagestorageservice.dto.NewestMessagesListDTO
import com.yazikochesalna.messagestorageservice.exception.customexceptions.CoroutinesException
import com.yazikochesalna.messagestorageservice.model.db.Attachment
import com.yazikochesalna.messagestorageservice.model.db.BaseMessage
import com.yazikochesalna.messagestorageservice.model.db.convertor.CassandraEntitiesConvertor
import com.yazikochesalna.messagestorageservice.model.db.Message
import com.yazikochesalna.messagestorageservice.model.db.MessageByChat
import com.yazikochesalna.messagestorageservice.repository.AttachmentRepository
import com.yazikochesalna.messagestorageservice.repository.MessageByChatRepository
import com.yazikochesalna.messagestorageservice.repository.MessageRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
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
        chatServiceClient.checkUserInChat(userId = userId,chatId = chatId)

    fun getMessagesAroundCursor(
        userId: Long,
        chatId: Long,
        cursor: UUID?,
        limitUp: Int,
        limitDown: Int
    ): List<MessagesJsonFormatDTO> {
        if (!isChatMember(userId =userId, chatId = chatId)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        return getCassandraMessages(chatId =chatId, cursor = cursor, limitUp = limitUp, limitDown = limitDown)
    }

    private fun getCassandraMessages(
        chatId: Long,
        cursor: UUID?,
        limitUp: Int,
        limitDown: Int
    ): List<MessagesJsonFormatDTO> {
        val cassandraMessages = when (cursor) {
            null -> messageRepository.findMessagesWithoutCursor(chatId= chatId, limitUp =limitUp)
            else -> messageRepository.findMessagesByCursor(chatId =chatId, cursor = cursor, limitUp = limitUp, limitDown = limitDown)
        }.collectList()
            .block(Duration.ofSeconds(BLOCK_DURATION)) ?: emptyList()

        return kotlin.runCatching {
            runBlocking {
                mapMessagesWithAttachments(cassandraMessages) {
                        message, attachment -> cassandraEntitiesConvertor.convertToMessagesJsonFormatDto(message = message, attachments = attachment)
                }
            }
        }.getOrElse { e -> throw CoroutinesException("Unexpected error in fetching newest messages via coroutines", e) }


    }

    fun saveMessagesBatch(messages: List<MessagesJsonFormatDTO>): Mono<Void> {
        if (messages.isEmpty() || messages.size > BATCH_SIZE) {
            return Mono.error(IllegalArgumentException("Invalid batch size"))
        }

        val messagePairs = messages.map { cassandraEntitiesConvertor.convertToMessageWithAttachments(it) }
        val simpleMessages = messagePairs.map { it.first }
        val messagesByChat = messagePairs.map { cassandraEntitiesConvertor.convertToMessageByChat(it.first) }
        val attachments = messagePairs.flatMap { it.second }

        return saveMesagesToAllTables(messages = simpleMessages, messagesByChat =messagesByChat, attachments = attachments).then()
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


    fun getNewestMessagesByChat(chatList: List<Long>): NewestMessagesListDTO {
        return NewestMessagesListDTO(kotlin.runCatching {
            val lastMessages = chatList.mapNotNull { chatId ->
                messageByChatRepository.findFirstByChatId(chatId).block()
            }
            val messagesWithAttachments:  List<MessagesJsonFormatDTO>
            runBlocking {
                messagesWithAttachments = mapMessagesWithAttachments(lastMessages) { message, attachments ->
                    cassandraEntitiesConvertor.convertToMessagesJsonFormatDto(message, attachments)
                }
            }
            // Собираем результат
            chatList.map { chatId ->
                NewestMessageDTO(
                    chatId = chatId,
                    lastMessage = messagesWithAttachments.find { it.payload.chatId  == chatId }
                )
            }
        }.getOrElse { e -> throw CoroutinesException("Unexpected error in fetching newest messages via coroutines", e) }
        )

    }


    private suspend fun mapMessagesWithAttachments(
        messages: List<BaseMessage>,
        converter: (BaseMessage, List<Attachment>) -> MessagesJsonFormatDTO
    ): List<MessagesJsonFormatDTO> {
        if (messages.isEmpty()) return emptyList()

        val messageIds = messages.map { it.id }
        val attachmentsMap = attachmentRepository.findByMessageIdIn(messageIds)
            .collectMultimap { it.messageId }
            //.block(Duration.ofSeconds(BLOCK_DURATION))
            .awaitFirstOrNull()
            ?.mapValues { it.value.toList() }
            ?: emptyMap()

        return messages.map { message ->
            converter(message, attachmentsMap[message.id] ?: emptyList())
        }
    }
}