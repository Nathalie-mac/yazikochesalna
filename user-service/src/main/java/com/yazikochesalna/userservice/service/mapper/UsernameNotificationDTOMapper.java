package com.yazikochesalna.userservice.service.mapper;

import com.yazikochesalna.userservice.dto.notificationdto.EventType;
import com.yazikochesalna.userservice.dto.notificationdto.NotificationDTO;
import com.yazikochesalna.userservice.dto.notificationdto.impl.UserUsernameUpdatePayloadDTO;

public class UsernameNotificationDTOMapper {

    public static NotificationDTO convertUpdateUserRequestDTOToNotificationDTO
            (Long id, String username){
        UserUsernameUpdatePayloadDTO payload = new UserUsernameUpdatePayloadDTO();
        payload.setUserId(id);
        payload.setUsername(username);

        NotificationDTO notification = new NotificationDTO();
        notification.setType(EventType.NEW_USERNAME);
        notification.setPayload(payload);

        return notification;
    }
}
