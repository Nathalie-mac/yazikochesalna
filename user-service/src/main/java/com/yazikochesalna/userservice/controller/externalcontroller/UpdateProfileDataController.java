package com.yazikochesalna.userservice.controller.externalcontroller;

import com.yazikochesalna.userservice.dto.UpdateUserRequestDTO;
import com.yazikochesalna.userservice.dto.UpdateUserResponseDTO;
import com.yazikochesalna.userservice.service.externalservice.UpdateProfileDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User API")
public class UpdateProfileDataController {

   private final UpdateProfileDataService updateProfileDataService;

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
             @RequestBody UpdateUserRequestDTO updateDTO
    ) {
        UpdateUserResponseDTO response = updateProfileDataService.updateUserProfile(id, updateDTO);
        return ResponseEntity.ok(response);
       }
}
