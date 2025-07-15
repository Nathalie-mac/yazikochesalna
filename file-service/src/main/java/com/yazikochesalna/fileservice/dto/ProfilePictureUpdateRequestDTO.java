package com.yazikochesalna.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePictureUpdateRequestDTO {

    private Long userID;
    private String fileUUID;

}
