package com.yazikochesalna.userservice.data.entity;

import com.yazikochesalna.userservice.component.UserEntityListener;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;


@Entity
@EntityListeners(UserEntityListener.class)
@Table(name = "users")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 50, unique = true, nullable = false)
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, message = "Username must be less than 50 characters")
    private String username;

    @Column(name = "file_uuid")
    private UUID fileUuid;

    @Column(name = "last_name", length = 50)
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Column(name = "first_name", length = 50)
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @Column(name = "middle_name", length = 50)
    @Size(max = 50, message = "Middle name must be less than 50 characters")
    private String middleName;

    @Column(name = "phone", length = 20)
    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{7,20}$", message = "Phone number is invalid")
    private String phone;

    @Column(name = "description")
    private String description;

    @Column(name = "birth_date")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

}
