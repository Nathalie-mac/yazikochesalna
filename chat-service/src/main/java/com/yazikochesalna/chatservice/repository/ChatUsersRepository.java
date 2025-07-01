package com.yazikochesalna.chatservice.repository;

import com.yazikochesalna.chatservice.model.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatUsersRepository extends JpaRepository<ChatUser, Long> {

    ChatUser getChatUserByChatIdAndUserId(long chatId, long userId);

    List<ChatUser> getChatUsersByChatId(long chatId);
}