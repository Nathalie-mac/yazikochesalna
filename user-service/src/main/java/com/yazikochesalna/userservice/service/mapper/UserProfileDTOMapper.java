package com.yazikochesalna.userservice.service.mapper;

import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.dto.UserProfileDTO;

public class UserProfileDTOMapper {

    public static UserProfileDTO convertUserToUserProfileDTO(Users users){
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUserName(users.getUsername());

        return userProfileDTO;
    }
}
