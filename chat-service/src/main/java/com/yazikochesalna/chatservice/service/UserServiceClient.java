package com.yazikochesalna.chatservice.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceClient {

    public List<Long> getExitingUsers(List<Long> checkingUsers) {
        return checkingUsers.stream().filter((id) -> id < 10).toList();
    }
}
