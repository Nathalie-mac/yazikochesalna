package com.yazikochesalna.userservice.controller.externalcontroller;

import com.yazikochesalna.userservice.advice.ValidationCustomException;
import com.yazikochesalna.userservice.data.repository.UsersRepository;
import com.yazikochesalna.userservice.dto.FileUpdateRequestDTO;
import com.yazikochesalna.userservice.dto.notificationdto.EventType;
import com.yazikochesalna.userservice.dto.notificationdto.NotificationDTO;
import com.yazikochesalna.userservice.dto.notificationdto.impl.UserAvatarUpdatePayloadDTO;
import com.yazikochesalna.userservice.service.externalservice.FileUserService;
import com.yazikochesalna.userservice.service.externalservice.MessagingClientService;
import com.yazikochesalna.userservice.service.mapper.AvatarNotificationDTOMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Internal file user API", description = "Внутренние методы управления пользователями для файл сервиса")
public class FileUserController {

    private final FileUserService fileUserService;
    private final MessagingClientService messagingClientService;

    @PatchMapping("/update-file")
    public ResponseEntity<Void> updateFileUuid(
            @RequestBody FileUpdateRequestDTO requestDTO) throws ServiceUnavailableException {

        if (requestDTO.getUserID() == null) {
            throw new ValidationCustomException("User id can not be null");
        }
        if (requestDTO.getFileUUID() == null) {
            throw new ValidationCustomException("File uuid can not be null");
        }

        fileUserService.updateUserFileUuid(requestDTO.getUserID(), requestDTO.getFileUUID());

        NotificationDTO notification =
                AvatarNotificationDTOMapper.convertFileUpdateRequestDTOToNotificationDTO(requestDTO);
        messagingClientService.setNewAvatar(notification);

        return ResponseEntity.noContent().build();
    }
}
