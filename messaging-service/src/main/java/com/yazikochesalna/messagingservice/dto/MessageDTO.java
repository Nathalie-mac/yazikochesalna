package com.yazikochesalna.messagingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class MessageDTO {
    protected String action;

    protected Long requestId;

}