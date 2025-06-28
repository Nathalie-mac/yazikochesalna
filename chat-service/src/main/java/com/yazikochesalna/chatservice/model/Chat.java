package com.yazikochesalna.chatservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="chat")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String type;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = true)
    private GroupChatDetails groupChatDetails;
}
