package com.yazikochesalna.messagingservice.dto.events.payload.chat.impl;

import com.yazikochesalna.messagingservice.dto.events.AttachmentDTO;
import com.yazikochesalna.messagingservice.dto.events.payload.chat.ChatPayloadDTO;
import com.yazikochesalna.messagingservice.dto.validator.ValidAttachmentLimits;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ChatMessagePayloadDTO extends ChatPayloadDTO {
    private Long senderId;
    @NotBlank(message = "Текст сообщения не может быть пустым")
    private String text;
    @Valid
    @ValidAttachmentLimits
    private List<AttachmentDTO> attachments;
}
