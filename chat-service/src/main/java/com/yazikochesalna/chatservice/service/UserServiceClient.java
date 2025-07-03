package com.yazikochesalna.chatservice.service;

import com.yazikochesalna.chatservice.exception.InvalidUserIdException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//mock service
public class UserServiceClient {

    public void validateUserId(long userId) {
        if (userId < 10) {
            return;
        }
        throw new InvalidUserIdException();
    }

    public List<Long> getExitingUsers(List<Long> checkingUsers) {
        return checkingUsers.stream().filter((id) -> id < 10).toList();
    }
}
