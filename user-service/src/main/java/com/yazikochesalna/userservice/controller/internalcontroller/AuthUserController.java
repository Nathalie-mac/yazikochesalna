package com.yazikochesalna.userservice.controller.internalcontroller;

import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.dto.*;
import com.yazikochesalna.userservice.service.internalservice.AuthUserService;
import com.yazikochesalna.userservice.service.mapper.ListIdsMapper;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Internal user API for auth", description = "Внутренние методы управления пользователями для сервиса авторизации")
public class AuthUserController {

    private final AuthUserService authUserService;

    @PostMapping("/check")
    @RolesAllowed("SERVICE")
    @Hidden
    public CheckUsersResponseDTO checkUsersExistence(@RequestBody CheckUsersRequestDTO checkUsersRequest) {
        List<Users> existingUsers = authUserService.findAllByIdIn(checkUsersRequest.usersIds());
        return new CheckUsersResponseDTO(ListIdsMapper.mapUsersToIds(existingUsers));
    }

    @PostMapping
    @RolesAllowed("SERVICE")
    @Hidden
    public ResponseEntity<CreateUserResponseDTO> createUser(
            @RequestBody CreateUserRequestDTO request) {

        Users newUser = authUserService.createUser(request.getUsername());

        CreateUserResponseDTO createUserResponseDTO = new CreateUserResponseDTO(newUser.getId());
        return ResponseEntity.ok(createUserResponseDTO);
    }
}
