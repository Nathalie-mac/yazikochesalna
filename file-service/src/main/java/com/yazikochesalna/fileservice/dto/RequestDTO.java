package com.yazikochesalna.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    private String fileUUID;

    private String messageUUID;
    private Long chatID;
    private Long userID;
}
