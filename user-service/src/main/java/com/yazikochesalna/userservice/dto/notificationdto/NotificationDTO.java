package com.yazikochesalna.userservice.dto.notificationdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private EventType type;

    protected PayloadDTO payload;

    public <T extends PayloadDTO> T getPayload() {
        return (T) payload;
    }
}
