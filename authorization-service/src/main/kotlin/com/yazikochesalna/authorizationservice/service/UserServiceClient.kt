package com.yazikochesalna.authorizationservice.com.yazikochesalna.authorizationservice.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.util.*

@RequiredArgsConstructor
@Service
//mock service
class UserServiceClient {
    public fun saveUser(username: String): Long {
        return Random().nextLong();
    }
}
