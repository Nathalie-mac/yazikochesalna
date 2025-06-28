package com.yazikochesalna.chatservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="chat_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatUsers {
    @Id
    private String id;

    private String userId;
}
