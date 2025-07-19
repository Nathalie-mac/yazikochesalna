package com.yazikochesalna.chatservice.service;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public interface MessagingServiceClient {

    void sendMemberAddedNotification(long userId, long chatId);

    void sendMemberRemovedNotification(long userId, long chatId);

    default void sendMemberRemovedNotification(Set<Long> usersIds, long chatId) {
        for (Long userId: usersIds) {
            sendMemberRemovedNotification(userId, chatId);
        }
    }

    void sendAvatarUpdatedNotification(long chatId, long userId, UUID avatarUuid);
}
