package com.yazikochesalna.chatservice.repository;

import com.yazikochesalna.chatservice.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChats extends JpaRepository<GroupChat, Long> {

}