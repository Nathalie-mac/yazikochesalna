package com.yazikochesalna.chatservice.model;

import com.yazikochesalna.chatservice.enums.ChatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    @Enumerated(EnumType.STRING)
    private ChatType type;

    @OneToOne(cascade = CascadeType.ALL, optional = true, mappedBy = "chat")
    private GroupChatDetails groupChatDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chat", orphanRemoval = true)
    private List<ChatUser> members = new ArrayList<>();


    public Chat setMembers(List<ChatUser> members) {
        this.members = new CopyOnWriteArrayList<>(members);
        return this;
    }

    public void addMember(ChatUser member) {
        members.add(member);
        member.setChat(this);
    }

}
