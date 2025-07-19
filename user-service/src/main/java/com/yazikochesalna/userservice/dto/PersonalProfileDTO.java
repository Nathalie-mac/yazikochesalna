package com.yazikochesalna.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalProfileDTO {

    private String userName;
    private long userId;
    private String login;
}
