package com.yazikochesalna.messagingservice.dto.kafka;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayloadMessageDTO extends PayloadDTO {
    private Long senderId;

    @NotNull(message = "chatId не может быть null")
    private Long chatId;
    @NotBlank(message = "Текст сообщения не может быть пустым")
    private String text;
}
