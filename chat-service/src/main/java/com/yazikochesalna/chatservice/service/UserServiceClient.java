package com.yazikochesalna.chatservice.service;

import com.yazikochesalna.chatservice.exception.InvalidUserIdException;

import java.util.List;
import java.util.Set;

public interface UserServiceClient {

    void validateUserId(long userId) throws InvalidUserIdException;

    Set<Long> getExitingUsers(Set<Long> checkingUsers);

    void validateUsers(Set<Long> checkingUsers) throws InvalidUserIdException;

    default void validateUsers(List<Long> checkingUsers) throws InvalidUserIdException {
        validateUsers(Set.copyOf(checkingUsers));
    }
}
