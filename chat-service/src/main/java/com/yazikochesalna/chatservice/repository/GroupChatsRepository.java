package com.yazikochesalna.chatservice.repository;

import com.yazikochesalna.chatservice.model.GroupChatDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatsRepository extends JpaRepository<GroupChatDetails, Long> {

}