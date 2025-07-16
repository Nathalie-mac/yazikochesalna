package com.yazikochesalna.messagingservice.dto.kafka;

import com.yazikochesalna.messagingservice.dto.validator.ValidAttachmentLimits;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

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
    @Valid
    @ValidAttachmentLimits
    private List<AttachmentDTO> attachments;
}
