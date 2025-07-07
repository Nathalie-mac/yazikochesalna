package com.yazikochesalna.messagestorageservice.service

import com.datastax.oss.driver.shaded.guava.common.collect.Lists
import com.yazikochesalna.messagestorageservice.converter.MessagesConverter
import com.yazikochesalna.messagestorageservice.dto.MessagesToFrontDTO
import com.yazikochesalna.messagestorageservice.model.db.Message
import com.yazikochesalna.messagestorageservice.repository.MessageRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.time.Duration

@Service
class MessageService(
    private val chatServiceClient: ChatServiceClient,
    private val messageRepository: MessageRepository){

    //TODO: таймаут добавить
    private fun isChatMember(userId: Long, chatId: Long): Boolean{
        return chatServiceClient.checkUserInChat(userId, chatId)
    //?: throw IllegalStateException("Failed to check chat membership")
    }

//    private fun getMessages(userId: Long, chatId: Long, cursor: UUID, limitUp: Int, limitDown: Int): Flux<MessagesToFrontDTO> {
//        val message = messageRepository.findMessagesByCursor(userId, chatId, cursor, limitUp, limitDown)
//        return
//    }

    fun getMessagesAroundCursor(userId: Long, chatId: Long, cursor: UUID, limitUp: Int, limitDown: Int): List<MessagesToFrontDTO>{
        if (!isChatMember(userId, chatId)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        //заглушка
        //var messagesToFrontDTOs = mutableListOf<MessagesToFrontDTO>()
        //messagesToFrontDTOs.add(MessagesToFrontDTO(UUID.randomUUID(), userId, chatId, LocalDateTime.now()))
        //return messagesToFrontDTOs
        //messageRepository.findMessagesByCursor().map { message -> MessagesConverter::convertMessageToMessageFrontDTO }
        return getCassandraMessages(chatId, cursor, limitUp, limitDown)
    }

    fun getCassandraMessages(chatId: Long, cursor: UUID, limitUp: Int, limitDown: Int): List<MessagesToFrontDTO>{
        val cassandraMessages = messageRepository.findMessagesByCursor(chatId, cursor, limitUp, limitDown)
        //превращаем в List<Message>
        return cassandraMessages.collectList()
            .block(Duration.ofSeconds(10))
            ?.map { message -> MessagesToFrontDTO(
                messageId = message.id,
                senderId = message.senderId,
                text = message.text,
                timestamp = message.sendTime) }
            ?: emptyList()
        //конвертируем в MessagesToFrontDTO (используя OpjectMapper)
    }
}