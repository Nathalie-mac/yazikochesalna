package com.yazikochesalna.chatservice.service.implementation;

import com.yazikochesalna.chatservice.config.properties.UserServiceProperties;
import com.yazikochesalna.chatservice.dto.messagestorageservice.LastMessageInChatDto;
import com.yazikochesalna.chatservice.dto.messagestorageservice.LastMessagesListDto;
import com.yazikochesalna.chatservice.dto.messagestorageservice.MessageDto;
import com.yazikochesalna.chatservice.service.MessageStorageServiceClient;
import com.yazikochesalna.common.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MessageStorageServiceClientImpl implements MessageStorageServiceClient {
    private static final String LAST_MESSAGES_IN_CHATS_URL_FORMAT = "%s/api/v1/messages/newest";
    private static final Logger logger = LoggerFactory.getLogger(MessageStorageServiceClient.class);

    private final JwtService jwtService;
    private final WebClient userServiceWebClient;
    private final UserServiceProperties userServiceProperties;

    public Map<Long, MessageDto> getLastMessages(List<Long> chatIds) {
        String url = String.format(LAST_MESSAGES_IN_CHATS_URL_FORMAT, userServiceProperties.getUrl());

        try {
            LastMessagesListDto messages = userServiceWebClient.post()
                    .uri(url)
                    .headers((headers) ->
                            headers.setBearerAuth(
                                    jwtService.generateServiceToken()
                            ))
                    .bodyValue(chatIds)
                    .retrieve()
                    .bodyToMono(LastMessagesListDto.class)
                    .block();

            assert messages != null;
            Map<Long, MessageDto> result = new Hashtable<>();
            return Stream.concat(
                            messages.messages().stream(),
                            chatIds.stream().map(chatId -> new LastMessageInChatDto(chatId, null))
                    )
                    .collect(Collectors.toMap(
                                    LastMessageInChatDto::chatId,
                                    LastMessageInChatDto::message,
                                    (existing, replacement) -> existing
                            )
                    );

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return chatIds.stream().map(chatId -> new LastMessageInChatDto(chatId, null)).collect(Collectors.toMap(
                            LastMessageInChatDto::chatId,
                            LastMessageInChatDto::message,
                            (existing, replacement) -> existing
                    )
            );
        }
    }

}
