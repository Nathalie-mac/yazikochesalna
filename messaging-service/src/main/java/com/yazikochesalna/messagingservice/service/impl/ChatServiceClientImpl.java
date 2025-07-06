package com.yazikochesalna.messagingservice.service.impl;

import com.yazikochesalna.common.service.JwtService;
import com.yazikochesalna.messagingservice.dto.chat.ChatUsersResponseDTO;
import com.yazikochesalna.messagingservice.service.ChatServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceClientImpl implements ChatServiceClient {
    public static final String CHECK_USER_IN_CHAT_URL_FORMAT = "%s/api/v1/chats/check/%d/%d";
    public static final String GET_USERS_URL_FORMAT = "%s/api/v1/chats/%d/members";
    private final WebClient chatServiceWebClient;
    private final JwtService jwtService;
    @Value("${chat.service.url}")
    private String serviceBaseUrl;

    //TODO: подумать над неблокирующими заросами


    @Override
    public boolean isUserInChat(Long userId, Long chatId) {
        var url = String.format(CHECK_USER_IN_CHAT_URL_FORMAT, serviceBaseUrl, chatId, userId);

        return Boolean.TRUE.equals(chatServiceWebClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(jwtService.generateServiceToken()))
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorReturn(false)
                .block());
    }

    @Override
    public List<Long> getUsersByChatId(Long chatId) {
        var url = String.format(GET_USERS_URL_FORMAT, serviceBaseUrl, chatId);

        return chatServiceWebClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(jwtService.generateServiceToken()))
                .retrieve()
                .bodyToMono(ChatUsersResponseDTO.class)
                .map(ChatUsersResponseDTO::getUsersIds)
                .onErrorReturn(Collections.emptyList())
                .block();
    }
}
