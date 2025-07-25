package com.yazikochesalna.chatservice.service.implementation;

import com.yazikochesalna.chatservice.config.properties.MessagingServiceProperties;
import com.yazikochesalna.chatservice.dto.messaginservice.MessagingServiceDefaultDto;
import com.yazikochesalna.chatservice.dto.messaginservice.NotificationType;
import com.yazikochesalna.chatservice.dto.messaginservice.payload.AvatarPayload;
import com.yazikochesalna.chatservice.dto.messaginservice.payload.MemberPayload;
import com.yazikochesalna.chatservice.service.MessagingServiceClient;
import com.yazikochesalna.common.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessagingServiceImp implements MessagingServiceClient {
    private static final String NOTIFICATION_URL_FORMAT = "%s/api/v1/ws/notification/update-members";
    private static final Logger logger = LoggerFactory.getLogger(MessagingServiceClient.class);

    private final JwtService jwtService;
    private final WebClient messagingServiceWebClient;
    private final MessagingServiceProperties messagingServiceProperties;

    private void sendNotification(MessagingServiceDefaultDto notification) {
        String url = String.format(NOTIFICATION_URL_FORMAT, messagingServiceProperties.getUrl());
        try {
            messagingServiceWebClient.post()
                    .uri(url)
                    .headers((headers) ->
                            headers.setBearerAuth(
                                    jwtService.generateServiceToken()
                            ))
                    .bodyValue(notification)
                    .retrieve()
                    .toBodilessEntity()
                    .doOnError(ex -> {
                        logger.error("Error occurred while calling URL: {}", url, ex);
                    })
                    .onErrorResume(ex -> Mono.empty()) // Игнорируем ошибки
                    .subscribe();

        } catch (Exception e) {
            //throw no exceptions because notifications is not important for logic
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void sendMemberAddedNotification(long userId, long chatId) {
        sendNotification(
                new MessagingServiceDefaultDto(
                        NotificationType.NEW_MEMBER,
                        new MemberPayload(
                                chatId,
                                userId
                        )
                ));
    }

    @Override
    public void sendMemberRemovedNotification(long userId, long chatId) {
        sendNotification(
                new MessagingServiceDefaultDto(
                        NotificationType.DROP_MEMBER,
                        new MemberPayload(
                                chatId,
                                userId
                        )
                ));
    }

    @Override
    public void sendAvatarUpdatedNotification(long chatId, long userId, UUID avatarUuid) {
        sendNotification(
                new MessagingServiceDefaultDto(
                        NotificationType.NEW_CHAT_AVATAR,
                        new AvatarPayload(
                                avatarUuid,
                                userId,
                                chatId
                        )
                )
        );

    }
}
