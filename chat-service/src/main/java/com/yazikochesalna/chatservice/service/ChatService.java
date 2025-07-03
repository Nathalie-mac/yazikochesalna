package com.yazikochesalna.chatservice.service;

import com.yazikochesalna.chatservice.dto.GetDialogResponseDto;
import com.yazikochesalna.chatservice.dto.GetGroupChatInfoDto;
import com.yazikochesalna.chatservice.dto.MembersListDto;
import com.yazikochesalna.chatservice.dto.chatList.ChatListDto;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatRequest;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatResponse;
import com.yazikochesalna.chatservice.enums.ChatType;
import com.yazikochesalna.chatservice.exception.ChatCreationException;
import com.yazikochesalna.chatservice.exception.DialogNotFoundException;
import com.yazikochesalna.chatservice.exception.InvalidUserIdException;
import com.yazikochesalna.chatservice.mapper.MapperToChatInList;
import com.yazikochesalna.chatservice.mapper.MapperToGetGroupChatInfoDto;
import com.yazikochesalna.chatservice.model.Chat;
import com.yazikochesalna.chatservice.model.ChatUser;
import com.yazikochesalna.chatservice.model.GroupChatDetails;
import com.yazikochesalna.chatservice.repository.ChatRepository;
import com.yazikochesalna.chatservice.repository.ChatUsersRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatUsersRepository chatUsersRepository;

    private final UserServiceClient userService;

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
            if (userService.getExitingUsers(request.memberIds()).size() != request.memberIds().size()) {
                throw new InvalidUserIdException();
            }
            List<ChatUser> members = request.memberIds().stream().map(userId -> new ChatUser(null, userId, null, chat)).toList();
            chat.setMembers(members);
            if (!request.memberIds().contains(ownerId)) {
                chat.addMember(new ChatUser(null, ownerId, null, chat));
            }
        } else {
            chat.addMember(new ChatUser(null, ownerId, null, chat));
        }
        chatRepository.save(chat);
        return new CreateChatResponse(chat.getId());
    }

    public ChatUser getUserInChat(long chatId, long userId) {
        return chatUsersRepository.getChatUserByChatIdAndUserId(chatId, userId);
    }

    public GetGroupChatInfoDto getGroupChatDetails(long currentUserId, long chatId) {
        Chat chat = chatRepository.getChatById(chatId);
        if (chat.getGroupChatDetails() == null) {
            return null;
        }
        return mapperToGetGroupChatInfoDto.toGroupChatInfoDto(chat);
    }

    public MembersListDto getChatMembers(final long chatId) {
        final List<ChatUser> users = chatUsersRepository.getChatUsersByChatId(chatId);
        final MembersListDto membersList = new MembersListDto(users.stream().map(ChatUser::getUserId).toList());
        return membersList;
    }

    public boolean addMembers(long ownerId, @NotNull Long chatId, @NotNull List<Long> newMembersIds) {
        final Chat chat = chatRepository.getChatById(chatId);
        if (!isOwner(chat, ownerId)) {
            return false;
        }
        if (userService.getExitingUsers(newMembersIds).size() != newMembersIds.size()) {
            throw new InvalidUserIdException();
        }
        final List<ChatUser> members = chat.getMembers();
        final List<Long> currentMembersIds = members.stream().map(ChatUser::getUserId).toList();
        final List<ChatUser> newMembers = newMembersIds
                .stream()
                .filter(Objects::nonNull)
                .filter(id -> !currentMembersIds.contains(id))
                .map(userId -> new ChatUser(null, userId, null, chat))
                .toList();
        members.addAll(newMembers);
        chatRepository.save(chat);
        return true;
    }

    public boolean removeMember(Long chatId, long ownerId, Long deletedUserId) {
        final Chat chat = chatRepository.getChatById(chatId);
        if (!isOwner(chat, ownerId)) {
            return false;
        }
        chat.getMembers().removeIf(chatUser -> chatUser.getUserId().equals(deletedUserId));
        chatRepository.save(chat);
        return true;
    }

    private boolean isOwner(final Chat chat, final Long userId) {
        if (chat == null) {
            return false;
        }
        return isOwner(chat.getGroupChatDetails(), userId);
    }

    private boolean isOwner(final GroupChatDetails details, final Long userId) {
        if (details == null) {
            return false;
        }
        return Objects.equals(details.getOwnerId(), userId);
    }

    public GetDialogResponseDto getDialog(final long userId, final long partnerId) {
        Long dialogInDB = findDialogId(userId, partnerId);
        if (dialogInDB != null) {
            return new GetDialogResponseDto(dialogInDB);
        }
        throw new DialogNotFoundException("No dialog between " + userId + " and " + partnerId);
    }

    public GetDialogResponseDto getOrCreateDialog(final long userId, final long partnerId) {
        Long dialogInDB = findDialogId(userId, partnerId);
        if (dialogInDB != null) {
            return new GetDialogResponseDto(dialogInDB);
        }

        return createDialog(userId, partnerId);
    }

    private GetDialogResponseDto createDialog(long userId, long partnerId) {
        Chat chat = new Chat(null, ChatType.PRIVATE, null, new LinkedList<>());
        chat.getMembers().add(new ChatUser(null, userId, null, chat));
        chat.addMember(new ChatUser(null, partnerId, null, chat));
        try {
            chatRepository.save(chat);
        } catch (DataIntegrityViolationException e) {
            //Ожидаемое исключение. Чат мог быть добавлен параллельно в другом потоке/запросе
        }
        Long createdChatId = findDialogId(userId, partnerId);
        if (createdChatId != null) {
            return new GetDialogResponseDto(createdChatId);
        }
        throw new ChatCreationException();
    }

    private Long findDialogId(final long user1, final long user2) {
        final List<Chat> chat = chatRepository.findDialogByUsers(user1, user2);
        if (chat.isEmpty()) {
            return null;
        }
        return chat.getFirst().getId();
    }
}
