package com.yazikochesalna.userservice.dto;

import com.yazikochesalna.userservice.data.entity.UserElasticsearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ElasticsearchResponseDTO {

    private List<UserElasticsearch> users;
}
