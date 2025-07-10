package com.yazikochesalna.messagestorageservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadMessageDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadNoticeDTO
import com.yazikochesalna.messagestorageservice.exception.customexceptions.ErrorInEnumException
import com.yazikochesalna.messagestorageservice.model.db.CassandraEntitiesConvertor
import com.yazikochesalna.messagestorageservice.model.db.Message
import com.yazikochesalna.messagestorageservice.model.db.MessageByChat
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import com.yazikochesalna.messagestorageservice.repository.MessageByChatRepository
import com.yazikochesalna.messagestorageservice.repository.MessageRepository
import org.springframework.stereotype.Service
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class MessageService(
    private val chatServiceClient: ChatServiceClient,
    private val messageRepository: MessageRepository,
    private val messageByChatRepository: MessageByChatRepository
    //private val messagesJSONSerializer: MessagesJSONSerializer
    ){
//    val objectMapper = ObjectMapper().apply {
//        //registerModule(KotlinModule())
//        registerModule(SimpleModule().addSerializer(Message::class.java, MessagesJSONSerializer()))
//    }
//    val serializer = MessagesJSONSerializer()
//    val jsonGenerator = objectMapper.createGenerator(StringWriter())

    val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
    val cassandraEntitiesConvertor = CassandraEntitiesConvertor()

    private fun isChatMember(userId: Long, chatId: Long): Boolean{
        return chatServiceClient.checkUserInChat(userId, chatId)
    }

    fun getMessagesAroundCursor(userId: Long, chatId: Long, cursor: UUID, limitUp: Int, limitDown: Int): List<MessagesJsonFormatDTO>{
        if (!isChatMember(userId, chatId)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
       return getCassandraMessages(chatId, cursor, limitUp, limitDown)
    }

    fun getCassandraMessages(chatId: Long, cursor: UUID, limitUp: Int, limitDown: Int): List<MessagesJsonFormatDTO>{
        val cassandraMessages = messageRepository.findMessagesByCursor(chatId, cursor, limitUp, limitDown)

        return cassandraMessages
            .map { message -> cassandraEntitiesConvertor.convertToMessagesJsonFormstDto(message)}
            .collectList()
            .block(Duration.ofSeconds(20)) ?: emptyList()
    }

    fun saveMessagesBatch(messages: List<MessagesJsonFormatDTO>): Mono<Void> {
        if (messages.isEmpty() || messages.size > 20) {
            return Mono.error(IllegalArgumentException("Invalid batch size"))
        }
        return saveMessagesToBothTables(
            Flux
                .fromIterable(messages
                    .map { message -> cassandraEntitiesConvertor.convertToMessage(message) })
        )
            .then()
    }


    fun saveMessagesToBothTables(messages: Flux<Message>): Flux<Message> {
        return messages.flatMap { message ->
            val messageByChat = cassandraEntitiesConvertor.convertToMessageByChat(message)
            Mono.zip(
                messageRepository.save(message),
                messageByChatRepository.save(messageByChat)
            ).thenReturn(message)
        }
    }


}