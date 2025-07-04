package com.yazikochesalna.userservice.service;

import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.data.repository.UsersRepository;
import com.yazikochesalna.userservice.dto.UserProfileDTO;
import com.yazikochesalna.userservice.advice.ResourceNotFoundCustomException;
import com.yazikochesalna.userservice.service.mapper.UserProfileDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UsersRepository usersRepository;

    public UserProfileDTO findUserProfile(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found with id: " + id));

        return UserProfileDTOMapper.convertUserToUserProfileDTO(user);
    }

}
