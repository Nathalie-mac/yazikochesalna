package com.yazikochesalna.userservice.service.mapper;

import com.yazikochesalna.userservice.data.entity.Users;

import java.util.List;
import java.util.stream.Collectors;

public class ListIdsMapper {

    public static List<Long>  mapUsersToIds(List<Users> users) {
        return users.stream()
                .map(Users::getId)
                .collect(Collectors.toList());
    }
}
