package com.yazikochesalna.userservice.service;

import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.data.repository.UsersRepository;
import com.yazikochesalna.userservice.dto.PersonalProfileDTO;
import com.yazikochesalna.userservice.dto.UserProfileDTO;
import com.yazikochesalna.userservice.advice.ResourceNotFoundCustomException;
import com.yazikochesalna.userservice.service.mapper.UserProfileDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UsersRepository usersRepository;
    private final AuthorizationClientService authorizationClientService;

    public UserProfileDTO findUserProfile(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found with id: " + id));

        return UserProfileDTOMapper.convertUserToUserProfileDTO(user);
    }

    public PersonalProfileDTO findPersonalProfileDTO(Long id)
            throws ServiceUnavailableException{

        Users user = findUser(id);
        String login = authorizationClientService.getUserLogin(id);
        PersonalProfileDTO profileDTO = new PersonalProfileDTO(user.getUsername(), login);
        return profileDTO;
    }

    private Users findUser (Long id){
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found with id: " + id));
        return user;
    }

}

