package com.yazikochesalna.messagestorageservice.service

import com.datastax.oss.driver.shaded.guava.common.collect.Lists
import com.yazikochesalna.messagestorageservice.dto.MessagesToFrontDTO
import com.yazikochesalna.messagestorageservice.repository.MessageRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlinx.coroutines.reactive.awaitSingle
import reactor.core.publisher.Flux

@Service
class MessageService(
    private val chatServiceClient: ChatServiceClient,
    private val messageRepository: MessageRepository){

    //TODO: сделать синхронным!
    private suspend fun isChatMember(userId: Long, chatId: Long): Boolean{
        return chatServiceClient.checkUserInChat(userId, chatId)
            .defaultIfEmpty(false)
            .awaitSingle()
    }

    private fun getMessages(userId: Long, chatId: Long, cursor: UUID, limitUp: Int, limitDown: Int): Flux<MessagesToFrontDTO> {
        val message = messageRepository.findMessagesByCursor(userId, chatId, cursor, limitUp, limitDown)
        return
    }


    fun getMessagesAroundCursor(userId: Long, chatId: Long, cursor: UUID, limitUp: Int, limitDown: Int): List<MessagesToFrontDTO>{
        var messagesToFrontDTOs = mutableListOf<MessagesToFrontDTO>()
        if (isChatMember(userId, chatId)){

        }
        return messagesToFrontDTOs
    }
}