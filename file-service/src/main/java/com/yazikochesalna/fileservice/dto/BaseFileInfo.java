package com.yazikochesalna.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseFileInfo {

      //  private Long id;
     private String storageFileName;
     //   private String createdBy;
     private String originalFileName;
     private String contentType;
     private Long size;
     private LocalDateTime uploadedAt;
     private String messageUuid;
     private Long chatUuid;
     private Long userId;

     private Map<String, Object> specificData;
}
