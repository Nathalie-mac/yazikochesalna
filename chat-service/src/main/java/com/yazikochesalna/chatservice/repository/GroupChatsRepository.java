package com.yazikochesalna.chatservice.repository;

import com.yazikochesalna.chatservice.model.GroupChatDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface GroupChatsRepository extends JpaRepository<GroupChatDetails, Long> {
//    @Query("SELECT gh.avatarUuid from GroupChatDetails gh where gh.chat.id = :chatId")
//    UUID findAvatarIdByChatId(UUID chatId);
}