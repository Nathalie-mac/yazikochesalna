package com.yazikochesalna.messagingservice.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
@Builder
public class ResponseDTO {

    private ResponseResultType result;
    private Long requestId;
    private UUID messageId;


}