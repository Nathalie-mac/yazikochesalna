package com.yazikochesalna.userservice.service.mapper;

import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.dto.UserProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileDTOMapper {

   UserProfileDTO toUserProfileDTO (Users user);

}
