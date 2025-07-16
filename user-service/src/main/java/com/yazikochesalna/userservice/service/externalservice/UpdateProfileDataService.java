package com.yazikochesalna.userservice.service.externalservice;

import com.yazikochesalna.userservice.advice.ResourceNotFoundCustomException;
import com.yazikochesalna.userservice.advice.UserAlreadyExistsCustomException;
import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.data.repository.UsersRepository;
import com.yazikochesalna.userservice.dto.UpdateUserRequestDTO;
import com.yazikochesalna.userservice.dto.UpdateUserResponseDTO;
import com.yazikochesalna.userservice.service.mapper.UploadUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateProfileDataService {

    private final UsersRepository usersRepository;
    private final UploadUserMapper uploadUserMapper;

    public UpdateUserResponseDTO updateUserProfile(Long id, UpdateUserRequestDTO updateDTO) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundCustomException(
                        String.format("Пользователь с %d не найден", id)));

        updateUserFields(user, updateDTO);
        Users updatedUser = usersRepository.save(user);

        return uploadUserMapper.toUpdateUserResponseDTO(updatedUser);
    }

    private void updateUserFields(Users user, UpdateUserRequestDTO updateDTO) {
        if (updateDTO.getUsername() != null) {
            validateUsernameUniqueness(updateDTO.getUsername(), user.getId());
            user.setUsername(updateDTO.getUsername());
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
            throw new UserAlreadyExistsCustomException("Username is already taken");
        }
    }
}
