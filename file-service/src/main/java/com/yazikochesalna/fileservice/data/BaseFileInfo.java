package com.yazikochesalna.fileservice.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseFileInfo {

     private String storageFileName;
     private String originalFileName;
     private String contentType;
     private Long size;
     private LocalDateTime lastModified;
     private String messageUuid;
     private Long chatId;
     private Long userId;

     private Map<String, Object> specificData;
}
