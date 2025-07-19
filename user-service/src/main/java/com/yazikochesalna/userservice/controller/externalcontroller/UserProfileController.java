package com.yazikochesalna.userservice.controller.externalcontroller;

import com.yazikochesalna.common.authentication.JwtAuthenticationToken;
import com.yazikochesalna.userservice.dto.PersonalProfileDTO;
import com.yazikochesalna.userservice.dto.UserProfileDTO;
import com.yazikochesalna.userservice.service.externalservice.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ServiceUnavailableException;

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

    @GetMapping("/me")
    @Operation(summary = "Получить личный профиль по id из jwt", description = "Возвращает userName и login пользователя")
    public ResponseEntity<PersonalProfileDTO> getPersonalProfile (HttpServletRequest request)
            throws ServiceUnavailableException{

        Long userId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        PersonalProfileDTO profileDTO = userProfileService.findPersonalProfileDTO(userId);

        return ResponseEntity.ok(profileDTO);
    }
}
