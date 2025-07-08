package com.yazikochesalna.messagestorageservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.yazikochesalna.messagestorageservice.dto.MessagesJSONSerializer
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.model.db.Message
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
    ){
    val objectMapper = ObjectMapper().apply {
        //registerModule(KotlinModule())
        registerModule(SimpleModule().addSerializer(Message::class.java, MessagesJSONSerializer()))
    }
    val serializer = MessagesJSONSerializer()
    val jsonGenerator = objectMapper.createGenerator(StringWriter())

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
            .map { message ->

                serializer.serialize(message, jsonGenerator, objectMapper.serializerProvider)
                objectMapper.readValue(jsonGenerator.outputTarget.toString(), MessagesJsonFormatDTO::class.java)
            }
            .collectList()
            .block(Duration.ofSeconds(20)) ?: emptyList()
    }
}