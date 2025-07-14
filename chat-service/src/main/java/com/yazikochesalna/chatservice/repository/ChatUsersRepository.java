package com.yazikochesalna.chatservice.repository;

import com.yazikochesalna.chatservice.model.ChatUser;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ChatUsersRepository extends JpaRepository<ChatUser, Long> {

    ChatUser getChatUserByChatIdAndUserId(long chatId, long userId);

    @Query("SELECT cu FROM ChatUser cu WHERE cu.chat.id = :chatId")
    List<ChatUser> getChatUsersByChatId(long chatId);

    @Modifying
    @Transactional
    @Query("UPDATE ChatUser cu SET cu.lastReadMessageId = :messageId WHERE cu.chat.id = :chatId AND cu.userId = :userId")
    int updateLastReadMessageId(@Param("chatId") long chatId, @Param("userId") long userId, @Param("messageId") @NotNull @NotEmpty UUID messageId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatUser record WHERE record.userId = :userId and record.chat.id = :chatId")
    int deleteByUserIdAndChatIdIfExits(@Param("chatId") long chatId, @Param("userId") Long userId);
}
