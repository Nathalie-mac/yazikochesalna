package com.yazikochesalna.messagingservice.service.impl;

import com.yazikochesalna.common.service.JwtService;
import com.yazikochesalna.messagingservice.dto.chat.ChatUsersResponseDTO;
import com.yazikochesalna.messagingservice.exception.ChatUserFetchCustomException;
import com.yazikochesalna.messagingservice.service.ChatServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceClientImpl implements ChatServiceClient {
    public static final String CHECK_USER_IN_CHAT_URL_FORMAT = "%s/check/%d/%d";
    public static final String GET_USERS_URL_FORMAT = "%s/%d/users";
    private final WebClient chatServiceWebClient;
    private final JwtService jwtService;
    @Value("${chat.service.url}")
    private String serviceBaseUrl;

    //TODO: подумать над неблокирующими заросами

    @Override
    public boolean isUserInChat(Long userId, Long chatId) {

        String url = String.format(CHECK_USER_IN_CHAT_URL_FORMAT, serviceBaseUrl, chatId, userId);

        return chatServiceWebClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generateServiceToken())
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorReturn(false)
                .timeout(Duration.ofSeconds(5))
                .block(Duration.ofSeconds(5));
    }

    @Override
    public List<Long> getUsersByChatId(Long chatId) {

        String url = String.format(GET_USERS_URL_FORMAT, serviceBaseUrl, chatId);

        return chatServiceWebClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generateServiceToken())
                .retrieve()
                .bodyToMono(ChatUsersResponseDTO.class)
                .map(ChatUsersResponseDTO::getUsersIds)
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(throwable -> new ChatUserFetchCustomException())
                .block(Duration.ofSeconds(6));
    }
}
