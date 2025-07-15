package com.yazikochesalna.userservice.service.internalservice;

import com.yazikochesalna.userservice.advice.UserAlreadyExistsCustomException;
import com.yazikochesalna.userservice.advice.ValidationCustomException;
import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.data.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final UsersRepository usersRepository;

    public List<Users> findAllByIdIn(List<Long> ids) {
        return usersRepository.findAllByIdIn(ids);
    }

    public Users createUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationCustomException("Username cannot be blank");
        }

        if (username.length() > 50) {
            throw new ValidationCustomException("Username must be less than 50 characters");
        }

        if (usersRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsCustomException("Username already exists");
        }

        Users user = new Users();
        user.setUsername(username);
        return usersRepository.save(user);
    }

}
