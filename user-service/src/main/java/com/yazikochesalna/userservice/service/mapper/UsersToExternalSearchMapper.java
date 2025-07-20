package com.yazikochesalna.userservice.service.mapper;

import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.dto.ExternalSearchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UsersToExternalSearchMapper {

    UsersToExternalSearchMapper INSTANCE = Mappers.getMapper(UsersToExternalSearchMapper.class);

    default ExternalSearchDTO toExternalSearchDTO(List<Users> usersList) {
        ExternalSearchDTO dto = new ExternalSearchDTO();
        if (usersList != null) {
            dto.setUserIds(usersList.stream()
                    .map(Users::getId)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
