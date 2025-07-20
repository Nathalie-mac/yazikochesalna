package com.yazikochesalna.userservice.service.externalservice;

import com.yazikochesalna.userservice.advice.ResourceNotFoundCustomException;
import com.yazikochesalna.userservice.advice.UserAlreadyExistsCustomException;
import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.data.repository.UsersRepository;
import com.yazikochesalna.userservice.dto.UpdateUserRequestDTO;
import com.yazikochesalna.userservice.dto.UpdateUserResponseDTO;
import com.yazikochesalna.userservice.service.mapper.UploadUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateProfileDataService {

    private final UsersRepository usersRepository;
    private final UploadUserMapper uploadUserMapper;

    @Transactional
    public UpdateUserResponseDTO updateUserProfile(Long id, UpdateUserRequestDTO updateDTO) {

        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundCustomException(
                        String.format("Пользователь с %d не найден", id)));

        updateUserFields(user, updateDTO);
        Users updatedUser = usersRepository.save(user);

        UpdateUserResponseDTO response = uploadUserMapper.toUpdateUserResponseDTO(updatedUser);

        return response;
    }

    private void updateUserFields(Users user, UpdateUserRequestDTO updateDTO) {
        if (updateDTO.getUsername() != null) {
            validateUsernameUniqueness(updateDTO.getUsername(), user.getId());
            user.setUsername(updateDTO.getUsername());
            user.setLow_username(updateDTO.getUsername().toLowerCase());
        }
        Optional.ofNullable(updateDTO.getLastName()).ifPresent(user::setLastName);
        Optional.ofNullable(updateDTO.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(updateDTO.getMiddleName()).ifPresent(user::setMiddleName);
        Optional.ofNullable(updateDTO.getPhone()).ifPresent(user::setPhone);
        Optional.ofNullable(updateDTO.getDescription()).ifPresent(user::setDescription);
        Optional.ofNullable(updateDTO.getBirthDate()).ifPresent(user::setBirthDate);
    }

    private void validateUsernameUniqueness(String username, Long userId) {
        if (usersRepository.existsByUsernameAndIdNot(username, userId)) {
            throw new UserAlreadyExistsCustomException("Такой username уже существует");
        }
    }
}
