package com.yazikochesalna.userservice.config;


import com.yazikochesalna.userservice.data.entity.Users;
import com.yazikochesalna.userservice.data.repository.UsersRepository;
import com.yazikochesalna.userservice.service.externalservice.ElasticsearchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ElasticsearchInitializerConfig {

    @Bean
    public CommandLineRunner loadData(UsersRepository usersRepository,
                                      ElasticsearchService elasticsearchService) {
        return args -> {
            elasticsearchService.recreateIndex();
            List<Users> users = usersRepository.findAll();
            elasticsearchService.indexAllUsers(users);
        };
    }
}
