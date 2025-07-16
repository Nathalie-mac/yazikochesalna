package com.yazikochesalna.messagingservice.dto.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadNotificationChangeMembersDTO extends PayloadDTO {
    @NotNull(message = "memberId не может быть null")
    private Long memberId;

}
