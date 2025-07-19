package com.yazikochesalna.chatservice.controller;

import com.yazikochesalna.chatservice.dto.*;
import com.yazikochesalna.chatservice.dto.chatList.ChatListDto;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatRequest;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatResponse;
import com.yazikochesalna.chatservice.dto.members.AddMembersRequest;
import com.yazikochesalna.chatservice.service.ChatService;
import com.yazikochesalna.common.authentication.JwtAuthenticationToken;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
@SecurityRequirement(name = "bearerAuth")
public class ChatsController {

    private final ChatService chatService;

    @GetMapping({"/", ""})
    @Operation(
            summary = "Получить список чатов пользователя",
            description = "Возвращает все чаты, в которых пользователь состоит"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Информация о чатах получена",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatListDto.class)
                    )}
            ),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = {@Content()})
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
    public ResponseEntity<CreateChatResponse> createGroupChat(@RequestBody @Valid @NotNull CreateChatRequest request){
        final long ownerId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        return ResponseEntity.ok(chatService.createGroupChat(request, ownerId));
    }

    @PostMapping({"/group/{chatId}/members","/group/{chatId}/members/"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи добавлены"),
            @ApiResponse(responseCode = "403", description = "Выполнение операции запрещено " +
                    "(пользователь не является владельцем чата или список пользователей содержит некорректных пользователей)")
    })
    public ResponseEntity<?> addMembersInChat(@PathVariable final Long chatId, @RequestBody @Valid final AddMembersRequest addMembersRequest) {
        final long ownerId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        final boolean result = chatService.addMembers(ownerId, chatId, addMembersRequest.newMembersIds());
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping({"/group/{chatId}/members/{deletedUserId}", "/group/{chatId}/members/{deletedUserId}"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь удалён из чата"),
            @ApiResponse(responseCode = "403", description = "Выполнение операции запрещено " +
                    "(пользователь не является владельцем чата)")
    })
    public ResponseEntity<?> removeMember(@PathVariable final Long chatId, @PathVariable final Long deletedUserId) {
        if (chatId == null || deletedUserId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        final long ownerId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        final boolean result = chatService.removeMember(chatId, ownerId, deletedUserId);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping({"/group/{chatId}","/group/{chatId}/"})
    public ResponseEntity<GetGroupChatInfoDto> getGroupChatDetails(@PathVariable Long chatId) {
        final long userId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        if (chatService.getUserInChat(chatId, userId) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        GetGroupChatInfoDto dto =  chatService.getGroupChatDetails(userId, chatId);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping ({"/dialog/{partnerId}","/dialog/{partnerId}/"})
    public ResponseEntity<GetDialogResponseDto> createDialog(@PathVariable Long partnerId) {
        final long userId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                chatService.getOrCreateDialog(userId, partnerId)
        );
    }

    @GetMapping ({"/dialog/{partnerId}","/dialog/{partnerId}/"})
    public ResponseEntity<GetDialogResponseDto> getDialog(@PathVariable Long partnerId) {
        final long userId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        return ResponseEntity.status(HttpStatus.OK).body(
                chatService.getDialog(userId, partnerId)
        );
    }


    @GetMapping({"/{chatId}","/{chatId}/"})
    public ResponseEntity<ShortChatInfoResponse> getShortChatInfo(@PathVariable long chatId) {
        final long userId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        final ShortChatInfoResponse info = chatService.getShortChatInfo(userId, chatId);
        if (info == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return  ResponseEntity.ok(info);
    }
    
    @PostMapping({"/{chatId}/lastRead", "/{chatId}/lastRead/"})
    public ResponseEntity<?> updateLastReadMessage(@PathVariable long chatId, @Valid @RequestBody UpdateLastReadMessageRequestDto updateLastReadMessageRequest){
        final long userId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        if (chatService.updateLastReadMessage(chatId, userId, updateLastReadMessageRequest.lastRead())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @PostMapping({"/{chatId}/newavatar", "/{chatId}/newavatar"})
    public ResponseEntity<?> updateChatAvatar(@PathVariable long chatId, @NotNull @RequestParam UUID avatarUuid){
        final long userId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        final Boolean update = chatService.updateChatAvatar(userId, chatId, avatarUuid);
        if (update) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping({"/check/{chatId}/{userId}", "/check/{chatId}/{userId}/"})
    @RolesAllowed("SERVICE")
    @Operation(summary = "Проверка на пользователя в чате",
            description = "Внтуренний метод для проверки наличия пользователя в чате. Доступен только для сервисов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь состоит в чате"),
            @ApiResponse(responseCode = "404", description = "Пользователь не состоит в чате")
    })
    @Hidden
    public ResponseEntity<?> checkUserInChat(@PathVariable long chatId, @PathVariable long userId) {
        if (chatService.getUserInChat(chatId, userId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping({"/{chatId}/members", "/{chatId}/members/"})
    @RolesAllowed("SERVICE")
    @Operation(summary = "Возвращает список участников чата",
            description = "Внтуренний метод для получения списка участников чата.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь состоит в чате"),
            @ApiResponse(responseCode = "404", description = "Пользователь не состоит в чате")
    })
    @Hidden
    public ResponseEntity<MembersListDto> getChatMembers(@PathVariable long chatId) {
        MembersListDto members = chatService.getChatMembers(chatId);
        if (members.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(members);
    }



}
