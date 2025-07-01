package com.yazikochesalna.chatservice.service;

import com.yazikochesalna.chatservice.dto.GetGroupChatInfoDto;
import com.yazikochesalna.chatservice.dto.chatList.ChatListDto;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatRequest;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatResponse;
import com.yazikochesalna.chatservice.enums.ChatType;
import com.yazikochesalna.chatservice.mapper.MapperToChatInList;
import com.yazikochesalna.chatservice.mapper.MapperToGetGroupChatInfoDto;
import com.yazikochesalna.chatservice.model.Chat;
import com.yazikochesalna.chatservice.model.ChatUser;
import com.yazikochesalna.chatservice.model.GroupChatDetails;
import com.yazikochesalna.chatservice.repository.ChatRepository;
import com.yazikochesalna.chatservice.repository.ChatUsersRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatUsersRepository chatUsers;
    private final MapperToChatInList mapperToChatInList;
    private final MapperToGetGroupChatInfoDto mapperToGetGroupChatInfoDto;

    public ChatListDto getUserChats(final long userId) {
        return new ChatListDto(
                chatRepository.findChatsByUser(userId).stream().map(chat -> mapperToChatInList.ChatToChatInListDto(chat, userId)).toList()
        );
    }

    public CreateChatResponse createGroupChat(@NotNull CreateChatRequest request, final long ownerId) {
        final GroupChatDetails details = new GroupChatDetails();
        details.setDescription(request.description());
        details.setName(request.title());
        details.setOwnerId(ownerId);
        final Chat chat = new Chat();

        chat.setType(ChatType.GROUP);
        chat.setGroupChatDetails(details);
        details.setChat(chat);

        if (request.memberIds() != null) {
            List<ChatUser> members = request.memberIds().stream().map(userId -> new ChatUser(null, userId, null, chat)).toList();
            chat.setMembers(members);
            if (!request.memberIds().contains(ownerId)) {
                chat.addMember(new ChatUser(null, ownerId, null, chat));
            }
        } else  {
            chat.addMember(new ChatUser(null, ownerId, null, chat));
        }
        chatRepository.save(chat);
        return new CreateChatResponse(chat.getId());
    }

    public ChatUser getUserInChat(long chatId, long userId) {
        return chatUsers.getChatUserByChatIdAndUserId(chatId, userId);
    }

    public GetGroupChatInfoDto getGroupChatDetails(long currentUserId, long chatId) {
        Chat chat =  chatRepository.getChatById(chatId);
        if (chat.getGroupChatDetails() == null) {
            return null;
        }
        return mapperToGetGroupChatInfoDto.toGroupChatInfoDto(chat);
    }
}
