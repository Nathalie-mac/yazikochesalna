package com.yazikochesalna.userservice.service.mapper;

import com.yazikochesalna.userservice.data.entity.UserElasticsearch;
import com.yazikochesalna.userservice.dto.ElasticsearchResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserElasticsearchMapper {

    UserElasticsearchMapper INSTANCE = Mappers.getMapper(UserElasticsearchMapper.class);

    default ElasticsearchResponseDTO toDto(List<UserElasticsearch> users) {
        ElasticsearchResponseDTO dto = new ElasticsearchResponseDTO();
        if (users != null) {
            dto.setUserIDs(users.stream()
                    .map(UserElasticsearch::getId)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
