package com.yazikochesalna.messagingservice.exception;

public class ChatUserFetchCustomException extends RuntimeException{
    public ChatUserFetchCustomException() {
        super("Failed to fetch users for chatId");
    }
}
