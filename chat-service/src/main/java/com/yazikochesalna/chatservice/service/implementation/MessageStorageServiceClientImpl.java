package com.yazikochesalna.chatservice.service.implementation;

import com.yazikochesalna.chatservice.config.properties.MessageStorageServiceProperties;
import com.yazikochesalna.chatservice.service.MessageStorageServiceClient;
import com.yazikochesalna.common.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageStorageServiceClientImpl implements MessageStorageServiceClient {
    private static final String LAST_MESSAGES_IN_CHATS_URL_FORMAT = "%s/api/v1/messages/newest";
    private static final Logger logger = LoggerFactory.getLogger(MessageStorageServiceClient.class);

    private final JwtService jwtService;
    private final WebClient userServiceWebClient;
    private final MessageStorageServiceProperties messageStorageServiceProperties;

    public Map<Long, Object> getLastMessages(List<Long> chatIds) {
        String url = String.format(LAST_MESSAGES_IN_CHATS_URL_FORMAT, messageStorageServiceProperties.getUrl());

        try {
            Map<String, Object> messages = userServiceWebClient.post()
                    .uri(url)
                    .headers((headers) ->
                            headers.setBearerAuth(
                                    jwtService.generateServiceToken()
                            ))
                    .bodyValue(chatIds)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            assert messages != null;
            List<Map<String, Object>> messagesList = (List<Map<String, Object>>) messages.get("messages") ;
            Map<Long, Object>  messagesMap = messagesList.stream()
                    .collect(Collectors.toMap(
                            entry -> ((Number) entry.get("chatId")).longValue(),
                            entry -> entry.get("lastMessage"),
                            (existing, replacement) -> existing
                    ));
            return chatIds.stream()
                    .collect(Collectors.toMap(
                            chatId -> chatId,
                            chatId -> messagesMap.getOrDefault(chatId, null)
                    ));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return chatIds.stream().collect(Collectors.toMap(
                            chatId -> chatId,
                            chatId -> new HashMap<>(),
                            (existing, replacement) -> existing
                    )
            );
        }
    }

}
