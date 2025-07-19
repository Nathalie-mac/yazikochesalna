package com.yazikochesalna.chatservice.mapper;

import com.yazikochesalna.chatservice.dto.chatList.ChatInListDto;
import com.yazikochesalna.chatservice.enums.ChatType;
import com.yazikochesalna.chatservice.model.Chat;
import com.yazikochesalna.chatservice.model.ChatUser;
import com.yazikochesalna.chatservice.model.GroupChatDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.lang.reflect.Member;
import java.util.Objects;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapperToChatInList {

    @Mapping( target = "chatId", source = "chat.id")
    @Mapping( target = "type", source = "chat.type")
    @Mapping( target = "title", expression = "java(generateTitle(chat))")
    @Mapping( target = "partnerId", expression = "java(getPartnerId(chat, thisUser))")
    @Mapping( target = "lastMessage", source = "lastMessage")
    @Mapping( target = "unreadCount", expression = "java(0)")
    ChatInListDto ChatToChatInListDto(Chat chat, long thisUser, Object lastMessage);

    default String generateTitle(Chat chat) {
        if (chat.getGroupChatDetails() == null) {
            return null;
        }
        if (chat.getType() == ChatType.GROUP) {
            return chat.getGroupChatDetails().getName();
        }
        return null;
    }

    default Long getPartnerId(Chat chat, Long thisUser) {
        if (thisUser == null) {
            return null;
        }
        if (chat.getType() != ChatType.PRIVATE) {
            return null;
        }
        for (ChatUser member : chat.getMembers()) {
            if (!Objects.equals(member.getUserId(), thisUser)) {
                return member.getUserId();
            }
        }
        return null;
    }
}
