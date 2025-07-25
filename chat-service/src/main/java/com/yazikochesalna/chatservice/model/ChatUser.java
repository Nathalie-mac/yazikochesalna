package com.yazikochesalna.chatservice.model;

import jakarta.persistence.*;
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
public class ChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long recordId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "last_read_message_id", nullable = true)
    private UUID lastReadMessageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;
}
