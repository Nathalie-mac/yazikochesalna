package com.yazikochesalna.messagingservice.exception;

public class UserNotHaveAccessToChatCustomException extends RuntimeException {
    public UserNotHaveAccessToChatCustomException() {
        super("The user is not a member of the requested chat and cannot send a message to it.");
    }
}