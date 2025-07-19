package com.yazikochesalna.userservice.dto;

import com.yazikochesalna.userservice.data.entity.Users;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponseDTO {

    private Long userId;
    private String username;
    private String lastName;
    private String firstName;
    private String middleName;
    private String phone;
    private String description;
    private LocalDate birthDate;

}
