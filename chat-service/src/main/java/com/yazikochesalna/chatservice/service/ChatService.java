package com.yazikochesalna.chatservice.service;

import com.yazikochesalna.chatservice.dto.chatList.ChatListDto;

import java.util.LinkedList;

public class ChatService {
    public ChatListDto getUserChats() {
        return new ChatListDto(new LinkedList<>());
    }
}
