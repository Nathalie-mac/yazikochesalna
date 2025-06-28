package com.yazikochesalna.chatservice.service;

import com.yazikochesalna.chatservice.dto.chatList.ChatListDto;
import com.yazikochesalna.chatservice.mapper.MapperToChatInList;
import com.yazikochesalna.chatservice.repository.ChatRepository;
import com.yazikochesalna.common.authentication.JwtAuthenticationToken;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@AllArgsConstructor
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MapperToChatInList mapperToChatInList;
    public ChatListDto getUserChats() {

        long userId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        return new ChatListDto(
                chatRepository.findChatsByUser(userId).stream().map(mapperToChatInList::ChatToChatInListDto).toList()
        );
    }
}
