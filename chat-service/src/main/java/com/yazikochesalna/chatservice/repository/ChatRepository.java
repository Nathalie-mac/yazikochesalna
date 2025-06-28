package com.yazikochesalna.chatservice.repository;

import com.yazikochesalna.chatservice.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("select ch from Chat ch left join ChatUser on Chat.id = ChatUser.chatId where ChatUser.chatId = :userId")
    List<Chat> findChatsByUser(long userId);
}