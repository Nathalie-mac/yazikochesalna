package com.yazikochesalna.userservice.dto;

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
public class UpdateUserRequestDTO {

    @Size(max = 50, message = "Username must be less than 50 characters")
    private String username;

    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name must be less than 50 characters")
    private String middleName;

    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{7,20}$", message = "Phone number is invalid")
    private String phone;

    private String description;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
}
