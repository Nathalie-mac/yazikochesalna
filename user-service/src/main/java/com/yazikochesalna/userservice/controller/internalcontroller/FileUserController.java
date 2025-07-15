package com.yazikochesalna.userservice.controller.internalcontroller;

import com.yazikochesalna.userservice.advice.ValidationCustomException;
import com.yazikochesalna.userservice.data.repository.UsersRepository;
import com.yazikochesalna.userservice.dto.FileUpdateRequestDTO;
import com.yazikochesalna.userservice.service.internalservice.FileUserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Internal file user API", description = "Внутренние методы управления пользователями для файл сервиса")
public class FileUserController {

    private final FileUserService fileUserService;
    private UsersRepository usersRepository;

    @PatchMapping("/update-file")
    @RolesAllowed("SERVICE")
    @Hidden
    public ResponseEntity<Void> updateFileUuid(
            @RequestBody FileUpdateRequestDTO requestDTO) {

        if (requestDTO.getUserID() == null) {
            throw new ValidationCustomException("User id can not be null");
        }
        if (requestDTO.getFileUUID() == null) {
            throw new ValidationCustomException("File uuid can not be null");
        }

        fileUserService.updateUserFileUuid(requestDTO.getUserID(), requestDTO.getFileUUID());
        return ResponseEntity.noContent().build();
    }
}
