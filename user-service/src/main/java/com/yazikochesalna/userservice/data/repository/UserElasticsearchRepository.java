package com.yazikochesalna.userservice.data.repository;

import com.yazikochesalna.userservice.data.entity.UserElasticsearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


import java.util.List;
import java.util.stream.Collectors;

public interface UserElasticsearchRepository extends ElasticsearchRepository<UserElasticsearch, Long> {

        @Query("""
            {
              "bool": {
                "should": [
                  {
                    "query_string": {
                      "query": "*?0*",
                      "fields": ["username", "lastName", "firstName", "middleName"]
                    }
                  },
                  {
                    "multi_match": {
                      "query": "?0",
                      "fields": ["username", "lastName", "firstName", "middleName"],
                      "fuzziness": "AUTO"
                    }
                  }
                ]
              }
            }
            """)
        List<UserElasticsearch> searchUsersLimited(String query, Pageable pageable);
}
