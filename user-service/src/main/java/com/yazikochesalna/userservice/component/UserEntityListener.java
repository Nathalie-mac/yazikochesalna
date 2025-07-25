package com.yazikochesalna.userservice.component;

import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.service.externalservice.ElasticsearchService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEntityListener {

    private final ElasticsearchService elasticsearchService;

    @PostPersist
    @PostUpdate
    public void postPersistOrUpdate(Users user) {
        elasticsearchService.indexUser(user);
    }

    @PostRemove
    public void postRemove(Users user) {
        elasticsearchService.deleteUserFromIndex(user.getId());
    }
}
