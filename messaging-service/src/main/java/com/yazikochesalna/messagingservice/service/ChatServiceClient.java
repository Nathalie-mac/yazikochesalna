package com.yazikochesalna.messagingservice.service;

import com.yazikochesalna.common.service.JwtService;
import com.yazikochesalna.messagingservice.config.properties.ChatServiceProperties;
import com.yazikochesalna.messagingservice.dto.chat.UsersResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceClient {
    public static final String CHECK_USER_IN_CHAT_URL_FORMAT = "%s/api/v1/chats/check/%d/%d";
    public static final String GET_USERS_BY_CHAT_ID_URL_FORMAT = "%s/api/v1/chats/%d/members";
    public static final String GET_USER_COMPANIONS_URL_FORMAT = "%s/api/v1/chats/companions/%d";
    private final WebClient chatServiceWebClient;
    private final JwtService jwtService;

    private final ChatServiceProperties chatServiceProperties;

    //TODO: подумать над неблокирующими заросами


    public boolean isUserInChat(Long userId, Long chatId) {
        var url = String.format(CHECK_USER_IN_CHAT_URL_FORMAT, chatServiceProperties.getUrl(), chatId, userId);

        return Boolean.TRUE.equals(chatServiceWebClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(jwtService.generateServiceToken()))
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorReturn(false)
                .block());
    }


    public List<Long> getUsersByChatId(Long chatId) {
        var url = String.format(GET_USERS_BY_CHAT_ID_URL_FORMAT, chatServiceProperties.getUrl(), chatId);

        return chatServiceWebClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(jwtService.generateServiceToken()))
                .retrieve()
                .bodyToMono(UsersResponseDTO.class)
                .map(UsersResponseDTO::getUserIds)
                .onErrorReturn(Collections.emptyList())
                .block();
    }

    public List<Long> getUserCompanionsByUserId(Long userId) {
        var url = String.format(GET_USER_COMPANIONS_URL_FORMAT, chatServiceProperties.getUrl(), userId);

        return chatServiceWebClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(jwtService.generateServiceToken()))
                .retrieve()
                .bodyToMono(UsersResponseDTO.class)
                .map(UsersResponseDTO::getUserIds)
                .onErrorReturn(Collections.emptyList())
                .block();
    }
}
