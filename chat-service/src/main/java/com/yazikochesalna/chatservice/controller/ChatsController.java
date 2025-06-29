package com.yazikochesalna.chatservice.controller;

import com.yazikochesalna.chatservice.dto.chatList.ChatListDto;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatRequest;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatResponse;
import com.yazikochesalna.chatservice.service.ChatService;
import com.yazikochesalna.common.authentication.JwtAuthenticationToken;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
public class ChatsController {

    private final ChatService chatService;

    @GetMapping({"/", ""})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о чатах получена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ResponseEntity<ChatListDto> getUserChats(){
        final long userId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        return ResponseEntity.ok(chatService.getUserChats(userId));
    }

    @PostMapping({"/group","/group/"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Чат создан"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ResponseEntity<CreateChatResponse> createGroupChat(@RequestBody @NotNull CreateChatRequest request){
        final long ownerId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        return ResponseEntity.ok(chatService.createGroupChat(request, ownerId));
    }
}
