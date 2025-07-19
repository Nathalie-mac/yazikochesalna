package com.yazikochesalna.chatservice.service;

import com.yazikochesalna.chatservice.dto.GetDialogResponseDto;
import com.yazikochesalna.chatservice.dto.GetGroupChatInfoDto;
import com.yazikochesalna.chatservice.dto.MembersListDto;
import com.yazikochesalna.chatservice.dto.ShortChatInfoResponse;
import com.yazikochesalna.chatservice.dto.chatList.ChatListDto;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatRequest;
import com.yazikochesalna.chatservice.dto.createChat.CreateChatResponse;
import com.yazikochesalna.chatservice.enums.ChatType;
import com.yazikochesalna.chatservice.exception.ChatCreationException;
import com.yazikochesalna.chatservice.exception.DialogNotFoundException;
import com.yazikochesalna.chatservice.mapper.MapperToChatInList;
import com.yazikochesalna.chatservice.mapper.MapperToGetGroupChatInfoDto;
import com.yazikochesalna.chatservice.model.Chat;
import com.yazikochesalna.chatservice.model.ChatUser;
import com.yazikochesalna.chatservice.model.GroupChatDetails;
import com.yazikochesalna.chatservice.repository.ChatRepository;
import com.yazikochesalna.chatservice.repository.ChatUsersRepository;
import com.yazikochesalna.chatservice.repository.GroupChatsRepository;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatUsersRepository chatUsersRepository;

    private final UserServiceClient userService;

    private final MapperToChatInList mapperToChatInList;
    private final MapperToGetGroupChatInfoDto mapperToGetGroupChatInfoDto;
    private final MessagingServiceClient messagingServiceClient;
    private final MessageStorageServiceClient messageStorageServiceClient;
    private final GroupChatsRepository groupChatsRepository;

    public ChatListDto getUserChats(final long userId) {
        List<Chat> chats = chatRepository.findChatsByUser(userId);
        List<Long> chatIds = chats.stream().map(Chat::getId).toList();
        Map<Long, Object> messagesMap = messageStorageServiceClient.getLastMessages(chatIds);
        return new ChatListDto(chats
                .stream().map(chat -> mapperToChatInList.ChatToChatInListDto(chat, userId, messagesMap.get(chat.getId()))).toList()
        );
    }

    public CreateChatResponse createGroupChat(@NotNull CreateChatRequest request, final long ownerId) {
        final GroupChatDetails details = new GroupChatDetails();
        details.setDescription(request.description());
        details.setName(request.title());
        details.setAvatarUuid(request.avatarUuid());
        details.setOwnerId(ownerId);
        final Chat chat = new Chat();

        chat.setType(ChatType.GROUP);
        chat.setGroupChatDetails(details);
        details.setChat(chat);

        if (request.membersIds() != null) {
            userService.validateUsers(request.membersIds());
            List<ChatUser> members = request.membersIds().stream().map(userId -> new ChatUser(null, userId, null, chat)).toList();
            chat.setMembers(members);
            if (!request.membersIds().contains(ownerId)) {
                chat.addMember(new ChatUser(null, ownerId, null, chat));
            }
        } else {
            chat.addMember(new ChatUser(null, ownerId, null, chat));
        }
        chatRepository.save(chat);
        chat.getMembers().forEach(member -> messagingServiceClient.sendMemberAddedNotification(member.getUserId(), chat.getId()));
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

    public boolean addMembers(long ownerId, @NotNull Long chatId, @NotNull Set<Long> newMembersIds) {
        final Chat chat = chatRepository.getChatById(chatId);
        if (!isOwner(chat, ownerId)) {
            return false;
        }
        userService.validateUsers(newMembersIds);

        for (Long newMemberId: newMembersIds) {
            ChatUser member = new ChatUser(null, newMemberId, null, chat);
            try {
                chatUsersRepository.save(member);
                messagingServiceClient.sendMemberAddedNotification(newMemberId, chatId);
            } catch (DataIntegrityViolationException e) {
                //do nothing, user was in chat or was added in other thread/instance
            }
        }
        return true;
    }

    public boolean removeMember(Long chatId, long ownerId, Long deletedUserId) {
        final Chat chat = chatRepository.getChatById(chatId);
        if (!isOwner(chat, ownerId)) {
            return false;
        }
        if (chatUsersRepository.deleteByUserIdAndChatIdIfExits(chatId, deletedUserId) > 0) {
            messagingServiceClient.sendMemberRemovedNotification(deletedUserId, chatId);
        }
        return true;
    }


    public ShortChatInfoResponse getShortChatInfo(long userId, long chatId) {
        final Chat chat = chatRepository.getChatById(chatId);
        if (chat == null) {
            return null;
        }
        final Optional<ChatUser> user =  chat.getMembers().stream().filter(chatUser -> chatUser.getUserId()==userId).findAny();
        if (user.isEmpty()) {
            return null;
        }
        Long ownerId = chat.getGroupChatDetails() != null ? chat.getGroupChatDetails().getOwnerId() : null;
        final List<Long> members = chat.getMembers().stream().map(ChatUser::getUserId).toList();
        final ShortChatInfoResponse response = new ShortChatInfoResponse(
                user.get().getLastReadMessageId(),
                chat.getType(),
                ownerId,
                members
        );
        return response;
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
        chat.addMember(new ChatUser(null, userId, null, chat));
        chat.addMember(new ChatUser(null, partnerId, null, chat));
        userService.validateUsers(List.of(userId, partnerId));
        try {
            chatRepository.save(chat);
            chat.getMembers().forEach(member -> messagingServiceClient.sendMemberAddedNotification(member.getUserId(), chat.getId()));
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

    public boolean updateLastReadMessage(long chatId, long userId, @NotNull @NotEmpty UUID messageId) {
        return chatUsersRepository.updateLastReadMessageId(chatId, userId, messageId) > 0;
    }

    //TODO: check!
    public Boolean updateChatAvatar(long userId, long chatId, @NotNull UUID avatarUuid) {
        Chat chat = chatRepository.getChatById(chatId);
        if (!isOwner(chat, userId)) {
            return false;
        }
        try{
            setNewChatAvatar(chat, avatarUuid);
            messagingServiceClient.sendAvatarUpdatedNotification(chatId, userId, avatarUuid);

        }catch(DataIntegrityViolationException e){
            return false;
        }
        return true;
    }

    //TODO: check!
    private Boolean setNewChatAvatar(Chat chat, @NotNull UUID uuid) {
        chat.getGroupChatDetails().setAvatarUuid(uuid);
        chatRepository.save(chat);
        return true;
    }
}
