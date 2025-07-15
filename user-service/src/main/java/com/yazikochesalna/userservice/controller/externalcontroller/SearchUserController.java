package com.yazikochesalna.userservice.controller.externalcontroller;

import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.dto.ExternalSearchDTO;
import com.yazikochesalna.userservice.service.externalservice.UserSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User API")
public class SearchUserController {

    private final UserSearchService userSearchService;

    @GetMapping("/search")
    @Operation(summary = "Получить пользователей по userName", description = "Возвращает максимум 40 первых пользователей")
    public ResponseEntity<ExternalSearchDTO> searchUsersByUsername(
            @RequestParam("username")
            @NotBlank
            String usernamePrefix) {

        List<Users> users = userSearchService.findUserIdsByUsernameStartsWith(usernamePrefix);
        ExternalSearchDTO searchDTO = new ExternalSearchDTO(users);

        return ResponseEntity.ok(searchDTO);
    }

}
