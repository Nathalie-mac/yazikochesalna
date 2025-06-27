package com.yazikochesalna.chatservice.repository;

import com.yazikochesalna.chatservice.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, String> {

}