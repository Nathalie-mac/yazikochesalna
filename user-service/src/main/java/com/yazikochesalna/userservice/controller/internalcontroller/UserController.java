package com.yazikochesalna.userservice.controller.internalcontroller;

import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.dto.CreateUserRequestDTO;
import com.yazikochesalna.userservice.dto.CreateUserResponseDTO;
import com.yazikochesalna.userservice.dto.SearchDTO;
import com.yazikochesalna.userservice.service.InternalUserService;
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
@Tag(name = "Internal user API", description = "Внутренние методы управления пользователями")
public class UserController {

    private final InternalUserService internalUserService;

    @PostMapping("/check")
    @RolesAllowed("SERVICE")
    @Hidden
    public SearchDTO checkUsersExistence(@RequestBody List<Long> userIds) {
        List<Users> existingUsers = internalUserService.findAllByIdIn(userIds);
        return new SearchDTO(ListIdsMapper.mapUsersToIds(existingUsers));
    }

    @PostMapping
    @RolesAllowed("SERVICE")
    @Hidden
    public ResponseEntity<CreateUserResponseDTO> createUser(
            @RequestBody CreateUserRequestDTO request) {

        Users newUser = internalUserService.createUser(request.getUsername());
        CreateUserResponseDTO createUserResponseDTO = new CreateUserResponseDTO(newUser.getId());
        return ResponseEntity.ok(createUserResponseDTO);
    }
}
