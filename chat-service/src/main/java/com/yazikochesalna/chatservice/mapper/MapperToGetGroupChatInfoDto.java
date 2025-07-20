package com.yazikochesalna.chatservice.mapper;

import com.yazikochesalna.chatservice.dto.GetGroupChatInfoDto;
import com.yazikochesalna.chatservice.model.Chat;
import com.yazikochesalna.chatservice.model.ChatUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {
            ChatUser.class
        })
public interface MapperToGetGroupChatInfoDto {
    @Mapping( target = "chatId", expression = "java(chat.getGroupChatDetails().getId())")
    @Mapping( target = "title", expression = "java(chat.getGroupChatDetails().getName())")
    @Mapping( target = "description", expression = "java(chat.getGroupChatDetails().getDescription())")
    @Mapping( target = "ownerId", expression = "java(chat.getGroupChatDetails().getOwnerId())")
    @Mapping( target = "membersIds", expression = "java(chat.getMembers().stream().map(ChatUser::getUserId).toList())")
    @Mapping( target = "avatarUuid", expression = "java(chat.getGroupChatDetails().getAvatarUuid())")
    GetGroupChatInfoDto toGroupChatInfoDto(Chat chat);
}
