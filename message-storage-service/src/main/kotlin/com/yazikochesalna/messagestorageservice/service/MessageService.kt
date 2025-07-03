package com.yazikochesalna.messagestorageservice.service

import com.datastax.oss.driver.shaded.guava.common.collect.Lists
import com.yazikochesalna.messagestorageservice.dto.MessagesToFrontDTO
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MessageService {
    private fun hasAccessToChat(userId: Long, chatId: Long): Boolean{
        return true
    }

    fun getMessagesAroundCursor(userId: Long, chatId: Long, cursor: UUID, limitUp: Int, limitDown: Int): List<MessagesToFrontDTO>{
        if (hasAccessToChat(userId, chatId)){

        }
        return Lists.newArrayList()
    }
}