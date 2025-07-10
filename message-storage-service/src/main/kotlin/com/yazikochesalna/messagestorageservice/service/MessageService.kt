package com.yazikochesalna.messagestorageservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadMessageDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadNoticeDTO
import com.yazikochesalna.messagestorageservice.exception.ErrorInMessageTypeException
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
import java.io.StringWriter
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
            .map { message -> convertToMessagesJsonFormstDto(message)}
            .collectList()
            .block(Duration.ofSeconds(20)) ?: emptyList()
    }

    fun saveMessagesBatch(messages: List<MessagesJsonFormatDTO>): Mono<Void>{
        if(messages.isEmpty() || messages.size > 20){return Mono.error(IllegalArgumentException("Invalid batch size"))}
        return saveMessagesToBothTables(Flux.fromIterable(messages.map { message -> message.toMessage() }))
            .then()
    }


    fun saveMessagesToBothTables(messages: Flux<Message>): Flux<Message> {
        return messages.flatMap { message ->
            val messageByChat = convertToMessageByChat(message)
            Mono.zip(
                messageRepository.save(message),
                messageByChatRepository.save(messageByChat)
            ).thenReturn(message)
        }
    }

    fun convertToMessagesJsonFormstDto(message: Message): MessagesJsonFormatDTO {
        return MessagesJsonFormatDTO(
            messageId = message.id,
            type = message.type,
            timestamp = message.sendTime,
            payload = when (message.type){
                MessageType.MESSAGE -> PayLoadMessageDTO(
                    senderId = message.senderId,
                    chatId = message.chatId,
                    text = message.text?: ""
                )
                MessageType.NEW_MEMBER,
                MessageType.DROP_MEMBER -> PayLoadNoticeDTO(
                    memberId = message.senderId,
                    chatId = message.chatId,
                )
                else -> throw IllegalArgumentException("Not supported message type ${message.type}")
            }
        )
    }

    fun MessagesJsonFormatDTO.toMessage(): Message {
        return when (type) {
            MessageType.MESSAGE -> {
                val payload = payload as PayLoadMessageDTO
                Message(
                    id = messageId,
                    type = type,
                    sendTime = timestamp,
                    senderId = payload.senderId,
                    chatId = payload.chatId,
                    text = payload.text
                )
            }
            MessageType.NEW_MEMBER, MessageType.DROP_MEMBER -> {
                val payload = payload as PayLoadNoticeDTO
                Message(
                    id = messageId,
                    type = type,
                    sendTime = timestamp,
                    senderId = payload.memberId,
                    chatId = payload.chatId,
                    text = null
                )
            }
            else -> throw ErrorInMessageTypeException("Not supported message type $type")
        }
    }

        fun convertToMessageByChat(message: Message): MessageByChat {
        return MessageByChat(
            id = message.id,
            chatId = message.chatId,
            senderId = message.senderId,
            sendTime = message.sendTime,
            text = message.text,
            type = message.type
        )
    }

}