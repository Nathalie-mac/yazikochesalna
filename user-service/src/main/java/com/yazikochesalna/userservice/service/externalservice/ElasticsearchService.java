package com.yazikochesalna.userservice.service.externalservice;

import com.yazikochesalna.userservice.data.entity.UserElasticsearch;
import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.data.repository.UserElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final UserElasticsearchRepository userElasticsearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Transactional
    public void indexAllUsers(List<Users> users) {
        List<UserElasticsearch> userSearches = users.stream()
                .map(user -> new UserElasticsearch(
                        user.getId(),
                        user.getUsername(),
                        user.getLastName(),
                        user.getFirstName(),
                        user.getMiddleName()))
                .collect(Collectors.toList());

        userElasticsearchRepository.saveAll(userSearches);
    }

    @Transactional
    public void indexUser(Users user) {
        UserElasticsearch userSearch = new UserElasticsearch(
                user.getId(),
                user.getUsername(),
                user.getLastName(),
                user.getFirstName(),
                user.getMiddleName());

        userElasticsearchRepository.save(userSearch);
    }

    public List<UserElasticsearch> searchUsers(String query) {
        return userElasticsearchRepository.searchUsersLimited(
                query, PageRequest.of(0, 40));
    }

    public void deleteUserFromIndex(Long id) {
        userElasticsearchRepository.deleteById(id);
    }


    public void recreateIndex() {
        elasticsearchOperations.indexOps(UserElasticsearch.class).delete();
        elasticsearchOperations.indexOps(UserElasticsearch.class).create();
    }

}
