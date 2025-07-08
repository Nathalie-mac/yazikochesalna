package com.yazikochesalna.chatservice.service.implementation;

import com.yazikochesalna.chatservice.config.properties.UserServiceProperties;
import com.yazikochesalna.chatservice.dto.userservice.CheckUsersRequest;
import com.yazikochesalna.chatservice.dto.userservice.CheckUsersResponse;
import com.yazikochesalna.chatservice.exception.InvalidUserIdException;
import com.yazikochesalna.chatservice.service.UserServiceClient;
import com.yazikochesalna.common.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceClientImpl implements UserServiceClient {
    private static final String EXITING_USERS_CHECK_URL_FORMAT = "%s/api/v1/users/check";
    private static final Logger logger = LoggerFactory.getLogger(UserServiceClientImpl.class);

    private final JwtService jwtService;
    private final WebClient userServiceWebClient;
    private final UserServiceProperties userServiceProperties;

    @Override
    public Set<Long> getExitingUsers(final Set<Long> checkingUsers) {
        String url = String.format(EXITING_USERS_CHECK_URL_FORMAT, userServiceProperties.getUrl());
        CheckUsersRequest request = new CheckUsersRequest(checkingUsers);

        try {
            CheckUsersResponse checkUserResponse = userServiceWebClient.post()
                    .uri(url)
                    .headers((headers) ->
                            headers.setBearerAuth(
                                    jwtService.generateServiceToken()
                            ))
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(CheckUsersResponse.class)
                    .block();

            assert checkUserResponse != null;
            return checkUserResponse.existingUsersIds();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

    @Override
    public void validateUserId(long userId) {
        if (getExitingUsers(Set.of(userId)).isEmpty()) {
            throw new InvalidUserIdException();
        }
    }

    @Override
    public void validateUsers(Set<Long> checkingUsers) {
        if (getExitingUsers(checkingUsers).size() != checkingUsers.size()) {
            throw new InvalidUserIdException();
        }
    }
}
