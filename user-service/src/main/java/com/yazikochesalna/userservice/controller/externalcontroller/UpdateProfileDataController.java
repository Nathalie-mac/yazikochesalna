package com.yazikochesalna.userservice.controller.externalcontroller;

import com.yazikochesalna.userservice.dto.UpdateUserRequestDTO;
import com.yazikochesalna.userservice.dto.UpdateUserResponseDTO;
import com.yazikochesalna.userservice.dto.notificationdto.NotificationDTO;
import com.yazikochesalna.userservice.service.externalservice.MessagingClientService;
import com.yazikochesalna.userservice.service.externalservice.UpdateProfileDataService;
import com.yazikochesalna.userservice.service.mapper.UsernameNotificationDTOMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User API")
public class UpdateProfileDataController {

   private final UpdateProfileDataService updateProfileDataService;
   private final MessagingClientService messagingClientService;

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequestDTO updateDTO
    ) throws ServiceUnavailableException {

        UpdateUserResponseDTO response = updateProfileDataService.updateUserProfile(id, updateDTO);

        if (updateDTO.getUsername() != null){
            NotificationDTO notification = UsernameNotificationDTOMapper.
                    convertUpdateUserRequestDTOToNotificationDTO(id, updateDTO.getUsername());
            messagingClientService.setNewUsername(notification);
        }
        return ResponseEntity.ok(response);
       }
}
