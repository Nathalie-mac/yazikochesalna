package com.yazikochesalna.chatservice.repository;

import com.yazikochesalna.chatservice.model.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUsers extends JpaRepository<ChatUser, Long> {

}