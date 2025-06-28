package com.yazikochesalna.chatservice.mapper;

import com.yazikochesalna.chatservice.dto.chatList.ChatInListDto;
import com.yazikochesalna.chatservice.model.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapperToChatInList {

    @Mapping( target = "chatId", source = "chat.id")
    @Mapping( target = "type", source = "chat.type")
    @Mapping( target = "title", expression = "java(chat.getGroupChatDetails() != null? chat.getGroupChatDetails().getName(): \"unknown\" )")
    @Mapping( target = "unreadCount", expression = "java(0)")
    ChatInListDto ChatToChatInListDto(Chat chat);
}
