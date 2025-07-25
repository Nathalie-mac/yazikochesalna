package com.yazikochesalna.messagingservice.dto.events;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;


@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AttachmentDTO {

    @NotNull(message = "id не может быть null")
    private UUID id;
    private AttachmentType type;

}
