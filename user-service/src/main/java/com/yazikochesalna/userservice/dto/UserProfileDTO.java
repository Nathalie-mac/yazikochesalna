package com.yazikochesalna.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

    private String username;

    private UUID fileUuid;
    private String lastName;
    private String firstName;
    private String middleName;
    private String phone;
    private String description;
    private LocalDate birthDate;
}
