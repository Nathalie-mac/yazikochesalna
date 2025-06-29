package com.yazikochesalna.chatservice.service;

import com.yazikochesalna.chatservice.dto.chatList.ChatListDto;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatRequest;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatResponse;
import com.yazikochesalna.chatservice.mapper.MapperToChatInList;
import com.yazikochesalna.chatservice.model.Chat;
import com.yazikochesalna.chatservice.model.ChatUser;
import com.yazikochesalna.chatservice.model.GroupChatDetails;
import com.yazikochesalna.chatservice.repository.ChatRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class ChatService {
    private static final String GROUP_CHAT_TYPE = "GROUP";
    private static final String PRIVATE_CHAT_TYPE = "PRIVATE";

    private final ChatRepository chatRepository;
    private final MapperToChatInList mapperToChatInList;

    public ChatListDto getUserChats(final long userId) {
        return new ChatListDto(
                chatRepository.findChatsByUser(userId).stream().map(mapperToChatInList::ChatToChatInListDto).toList()
        );
    }

    public CreateChatResponse createGroupChat(@NotNull CreateChatRequest request, final long ownerId) {
        final GroupChatDetails details = new GroupChatDetails();
        details.setDescription(request.description());
        details.setName(request.title());
        details.setOwnerId(ownerId);
        final Chat chat = new Chat();

        chat.setType(GROUP_CHAT_TYPE);
        chat.setGroupChatDetails(details);
        details.setChat(chat);

        if (request.memberIds() != null) {
            List<ChatUser> members = request.memberIds().stream().map(userId -> new ChatUser(0, userId, null, chat)).toList();
            chat.setMembers(members);
            if (!request.memberIds().contains(ownerId)) {
                chat.addMember(new ChatUser(0, ownerId, null, chat));
            }
        } else  {
            chat.addMember(new ChatUser(0, ownerId, null, chat));
        }
        chatRepository.save(chat);
        return new CreateChatResponse(chat.getId());
    }
}
