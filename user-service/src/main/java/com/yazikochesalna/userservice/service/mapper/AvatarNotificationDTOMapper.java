package com.yazikochesalna.userservice.service.mapper;

import com.yazikochesalna.userservice.dto.FileUpdateRequestDTO;
import com.yazikochesalna.userservice.dto.notificationdto.EventType;
import com.yazikochesalna.userservice.dto.notificationdto.NotificationDTO;
import com.yazikochesalna.userservice.dto.notificationdto.impl.UserAvatarUpdatePayloadDTO;

public class AvatarNotificationDTOMapper {

    public static NotificationDTO convertFileUpdateRequestDTOToNotificationDTO
            (FileUpdateRequestDTO requestDTO){
        UserAvatarUpdatePayloadDTO payload = new UserAvatarUpdatePayloadDTO();
        payload.setUserId(requestDTO.getUserID());
        payload.setAvatarId(requestDTO.getFileUUID());

        NotificationDTO notification = new NotificationDTO();
        notification.setType(EventType.NEW_USER_AVATAR);
        notification.setPayload(payload);

        return notification;
    }
}
