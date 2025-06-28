package com.yazikochesalna.chatservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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

    @Column(name = "last_read_message_id")
    UUID lastReadMessageId;
}
