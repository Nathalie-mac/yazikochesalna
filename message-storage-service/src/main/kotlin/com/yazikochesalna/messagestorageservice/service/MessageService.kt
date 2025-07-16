package com.yazikochesalna.messagestorageservice.service

import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.NewestMessageDTO
import com.yazikochesalna.messagestorageservice.model.db.CassandraEntitiesConvertor
import com.yazikochesalna.messagestorageservice.model.db.Message
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
    private val cassandraEntitiesConvertor: CassandraEntitiesConvertor
    //private val messagesJSONSerializer: MessagesJSONSerializer
) {
//    val objectMapper = ObjectMapper().apply {
//        //registerModule(KotlinModule())
//        registerModule(SimpleModule().addSerializer(Message::class.java, MessagesJSONSerializer()))
//    }
//    val serializer = MessagesJSONSerializer()
//    val jsonGenerator = objectMapper.createGenerator(StringWriter())

    //val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
    //val cassandraEntitiesConvertor = CassandraEntitiesConvertor()

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
        return saveMessagesToBothTables(
            Flux
                .fromIterable(messages
                    .map { message -> cassandraEntitiesConvertor.convertToMessage(message) })
        )
            .then()
    }


    private fun saveMessagesToBothTables(messages: Flux<Message>): Flux<Message> {
        return messages.flatMap { message ->
            val messageByChat = cassandraEntitiesConvertor.convertToMessageByChat(message)
            Mono.zip(
                messageRepository.save(message),
                messageByChatRepository.save(messageByChat)
            ).thenReturn(message)
        }
    }

    fun getNewestMessagesByChat(chatList: List<Long>): List<NewestMessageDTO> {
        return chatList.map { chatId ->
            val lastMessage = messageByChatRepository.findFirstByChatId(chatId).block()
            NewestMessageDTO(
                chatId = chatId,
                messagesJsonFormatDTO = lastMessage?.let { cassandraEntitiesConvertor.convertToMessagesJsonFormatDto(it) }
            )
        }
    }


}