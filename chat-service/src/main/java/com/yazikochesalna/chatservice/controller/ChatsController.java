package com.yazikochesalna.chatservice.controller;

import com.yazikochesalna.chatservice.dto.chatList.ChatInListDto;
import com.yazikochesalna.chatservice.dto.chatList.ChatListDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
public class ChatsController {

    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о чатах получена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ResponseEntity<ChatListDto> getUserSubscription(){
        return ResponseEntity.ok(chatSercvice.getUserSubscription());
    }
}
