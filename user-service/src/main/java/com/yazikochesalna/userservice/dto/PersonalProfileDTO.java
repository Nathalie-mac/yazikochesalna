package com.yazikochesalna.userservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class PersonalProfileDTO {

    private String userName;
    private long userId;
    private String login;

    private UUID fileUuid;
    private String lastName;
    private String firstName;
    private String middleName;
    private String phone;
    private String description;
    private LocalDate birthDate;

}
