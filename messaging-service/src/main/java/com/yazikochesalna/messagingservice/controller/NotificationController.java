package com.yazikochesalna.messagingservice.controller;

import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import com.yazikochesalna.messagingservice.service.WebSocketMessageService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ws/notification")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {
    private final WebSocketMessageService webSocketMessageService;

    @PostMapping("/")
    @RolesAllowed("SERVICE")
    @Operation(summary = "Отправка уведомления о изменении состава участников ",
            description = "Внтуренний метод для отправки уведомлений о добавлении или удалении нового пользователя. Доступен только для сервисов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомление принято в рассылку"),
            @ApiResponse(responseCode = "400", description = "Некорректный формат запроса")
    })
    @Hidden
    public ResponseEntity<?> sendUpdateChatMembersNotification(@Valid @RequestBody MessageDTO messageDTO) {
        webSocketMessageService.sendMessage(messageDTO);
        return ResponseEntity.ok().build();
    }


}
