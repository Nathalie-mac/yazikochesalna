package com.yazikochesalna.userservice.data.repository;

import com.yazikochesalna.userservice.data.entity.UserElasticsearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


import java.util.List;
import java.util.stream.Collectors;

public interface UserElasticsearchRepository extends ElasticsearchRepository<UserElasticsearch, Long> {

        @Query("""
            {
              "multi_match": {
                "query": "?0",
                "fields": ["username", "lastName", "firstName", "middleName"],
                "type": "best_fields",
                "fuzziness": "AUTO",
                "operator": "OR",
                "lenient": true
              }
            }
            """)
        List<UserElasticsearch> searchUsersLimited(String query, Pageable pageable);
}
