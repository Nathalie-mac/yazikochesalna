package com.yazikochesalna.userservice.service.mapper;

import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.dto.PersonalProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonalProfileMapper {

    @Mapping(target = "login", ignore = true)
    @Mapping(target = "userId", source = "id")
    PersonalProfileDTO toPersonalProfileDTO(Users user);
}
