package com.yazikochesalna.userservice.service.externalservice;

import com.yazikochesalna.userservice.advice.ResourceNotFoundCustomException;
import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.data.repository.UsersRepository;
import com.yazikochesalna.userservice.dto.notificationdto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUserService {

    private final UsersRepository usersRepository;

    public void updateUserFileUuid(Long userId, UUID fileUuid) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found"));

        usersRepository.updateFileUuid(userId, fileUuid);
    }

}
