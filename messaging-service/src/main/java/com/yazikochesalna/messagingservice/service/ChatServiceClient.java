package com.yazikochesalna.messagingservice.service;

import java.util.List;

public interface ChatServiceClient {
    boolean isUserInChat(Long userId, Long chatId);

    List<Long> getUsersByChatId(Long chatId);
}
