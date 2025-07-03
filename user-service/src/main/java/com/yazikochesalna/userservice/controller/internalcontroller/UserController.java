package com.yazikochesalna.userservice.controller.internalcontroller;

import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.service.InternalUserService;
import com.yazikochesalna.userservice.service.mapper.ListIdsMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Internal user API", description = "Внутренние методы управления пользователями")
public class UserController {

    private final InternalUserService internalUserService;

    @PostMapping("/check")
    @Operation(summary = "Проверка существования пользователей", description = "Возвращает список существующих пользователей")
    public List<Long> checkUsersExistence(@RequestBody List<Long> userIds) {
        List<Users> existingUsers = internalUserService.findAllByIdIn(userIds);
        return ListIdsMapper.mapUsersToIds(existingUsers);
    }

    @PostMapping
    @Operation(summary = "Создание пользователя в бд пользователей", description = "Возвращает id созданного пользователя")
    public ResponseEntity<Long> createUser(
            @RequestBody Map<String, String> request) {

        String username = request.get("username");
        Users newUser = internalUserService.createUser(username);
        return ResponseEntity.ok(newUser.getId());
    }
}
