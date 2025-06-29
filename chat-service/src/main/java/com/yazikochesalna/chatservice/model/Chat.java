package com.yazikochesalna.chatservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="chat")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String type;

    @OneToOne(cascade = CascadeType.ALL, optional = true, mappedBy = "chat")
    private GroupChatDetails groupChatDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chat")
    private List<ChatUser> members = new ArrayList<>();

    public void addMember(ChatUser member) {
        members.add(member);
        member.setChat(this);
    }

}
