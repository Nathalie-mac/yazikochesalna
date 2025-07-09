package com.yazikochesalna.chatservice.service;

import java.util.Set;

public interface MessagingServiceClient {

    void sendMemberAddedNotification(long userId, long chatId);
    void sendMemberRemovedNotification(long userId, long chatId);
    default void sendMemberRemovedNotification(Set<Long> usersIds, long chatId) {
        for (Long userId: usersIds) {
            sendMemberRemovedNotification(userId, chatId);
        }
    }
}
