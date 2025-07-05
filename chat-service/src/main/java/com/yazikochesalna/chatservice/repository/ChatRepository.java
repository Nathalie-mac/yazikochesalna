package com.yazikochesalna.chatservice.repository;

import com.yazikochesalna.chatservice.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("select ch from Chat ch left join ChatUser user on ch.id = user.chat.id where user.userId = :userId")
    List<Chat> findChatsByUser(long userId);

    Chat getChatById(long chatId);

    @Query("SELECT cu.chat " +
            "FROM ChatUser cu " +
            "JOIN cu.chat c " +
            "WHERE c.type = 'PRIVATE' AND cu.userId IN (:userId1, :userId2) " +
            "GROUP BY cu.chat.id " +
            "HAVING COUNT(DISTINCT cu.userId) = 2")
    List<Chat> findDialogByUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}