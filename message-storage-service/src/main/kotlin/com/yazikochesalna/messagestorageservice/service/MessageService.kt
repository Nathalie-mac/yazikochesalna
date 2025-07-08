package com.yazikochesalna.messagestorageservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadMessageDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadNoticeDTO
import com.yazikochesalna.messagestorageservice.model.db.Message
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import com.yazikochesalna.messagestorageservice.repository.MessageRepository
import org.springframework.stereotype.Service
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.io.StringWriter
import java.time.Duration

@Service
class MessageService(
    private val chatServiceClient: ChatServiceClient,
    private val messageRepository: MessageRepository,
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
}