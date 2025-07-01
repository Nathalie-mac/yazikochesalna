package com.yazikochesalna.chatservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="group_chats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatDetails {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Chat chat;
}
