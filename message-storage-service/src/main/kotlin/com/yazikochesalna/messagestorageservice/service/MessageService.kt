package com.yazikochesalna.messagestorageservice.service

import com.datastax.oss.driver.shaded.guava.common.collect.Lists
import com.yazikochesalna.messagestorageservice.dto.MessagesToFrontDTO
import java.util.UUID

class MessageService {
    fun getMessagesAroundCursor(userId: Long, chatId: Long, cursor: UUID, limitUp: Int, limitDown: Int): List<MessagesToFrontDTO>{
        return Lists.newArrayList()
    }
}