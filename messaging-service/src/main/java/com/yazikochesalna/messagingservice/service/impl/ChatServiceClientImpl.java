package com.yazikochesalna.messagingservice.service.impl;

import com.yazikochesalna.messagingservice.dto.chat.ChatUsersResponseDTO;
import com.yazikochesalna.messagingservice.service.ChatServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceClientImpl implements ChatServiceClient {
    public static final String CHECK_USER_IN_CHAT_URL_FORMAT = "%s/check/%d/%d";
    public static final String GET_USERS_URL_FORMAT = "%s/%d/users";
    private final RestTemplate restTemplate;
    @Value("${chat.service.url}")
    private String serviceBaseUrl;

    @Override
    public boolean isUserInChat(Long userId, Long chatId) {
        //TODO:получить сервисное JWT
        String url = String.format(CHECK_USER_IN_CHAT_URL_FORMAT, serviceBaseUrl, chatId, userId);

        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
            return response.getStatusCode().equals(HttpStatus.OK);
        } catch (RestClientException e) {
            return false;
        }
    }

    @Override
    public List<Long> getUsersByChatId(Long chatId) {
        //TODO: получить сервисное JWT
        String url = String.format(GET_USERS_URL_FORMAT, serviceBaseUrl, chatId);

        try {
            ResponseEntity<ChatUsersResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    ChatUsersResponseDTO.class
            );

            return response.getBody().getUsersIds();
        } catch (RestClientException e) {
            return Collections.emptyList();
        }
    }
}
