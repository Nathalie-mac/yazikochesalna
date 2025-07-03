package com.yazikochesalna.userservice.service;

import com.yazikochesalna.userservice.data.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSearchService {

    private final UsersRepository usersRepository;

    private static final int numberFoundUsers = 40;

    @Transactional(readOnly = true)
    public List<Long> findUserIdsByUsernameStartsWith(String usernamePrefix) {
        // Используем Pageable для ограничения количества результатов (максимум 40)
        Pageable limit = Pageable.ofSize(numberFoundUsers);
        return usersRepository.findUserIdsByUsernameStartingWith(usernamePrefix, limit);
    }
}
