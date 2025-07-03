package com.yazikochesalna.userservice.controller.externalcontroller;

import com.yazikochesalna.userservice.dto.UserProfileDTO;
import com.yazikochesalna.userservice.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "Управление пользователями")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{userId}")
    @Operation(summary = "Получить пользователя по id", description = "Возвращает userName пользователя")
    public  ResponseEntity<UserProfileDTO> getUserProfile (@PathVariable Long userId){
        UserProfileDTO profile = userProfileService.findUserProfile(userId);
        return ResponseEntity.ok(profile);
    }
}
