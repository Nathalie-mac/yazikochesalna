package com.yazikochesalna.userservice.data.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;


@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //возможно, убрать аннотации notBlank, size
    @Column(name = "username", length = 50, unique = true, nullable = false)
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, message = "Username must be less than 50 characters")
    private String username;

}
