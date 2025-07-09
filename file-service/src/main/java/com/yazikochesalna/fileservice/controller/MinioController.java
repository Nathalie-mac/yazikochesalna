package com.yazikochesalna.fileservice.controller;

import com.yazikochesalna.fileservice.service.FileMetadataFactory;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
@Tag(name = "Minio API")
public class MinioController {

    @Autowired
    private MinioClient minioClient;

    private final FileMetadataFactory fileMetadataFactory;

    @PostMapping("/upload")
    //public FileInfo uploadFile(
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam Map<String, String> metadata // Доп. метаданные из формы
    ) throws Exception {

        // Генерируем уникальный ключ для MinIO
        String objectName = "user-uploads/" + UUID.randomUUID();

        // 2. Извлекаем технические метаданные по Content-Type
        Map<String, String> extractedMetadata = fileMetadataFactory.extractMetadataByContentType(file);

        // Комбинируем метаданные
        Map<String, String> userMetadata = new HashMap<>();
        userMetadata.put("original-filename", file.getOriginalFilename());
        userMetadata.put("uploaded-at", Instant.now().toString());
        userMetadata.putAll(extractedMetadata);
        userMetadata.putAll(metadata); // Добавляем пользовательские метаданные

        // Загружаем в MinIO
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("mybucket")
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .userMetadata(userMetadata) // <- Важно!
                        .build()
        );

       // return new FileInfo(objectName, file.getOriginalFilename());
        // Возвращаем ID файла, заменить на дто!!!, возможно возвращать только ид без папки
        return ResponseEntity.ok(Map.of(
                "fileId", objectName
        ));

    }

    @GetMapping("/metadata/{fileId:.+}")
    public ResponseEntity<Map<String, String>> getFileMetadata(
            @PathVariable String fileId,
            @RequestParam(required = false) String bucket
    ) {
        try {
            String bucketName = bucket != null ? bucket : "mybucket";

            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object("user-uploads/" + fileId)
                            .build()
            );

            Map<String, String> metadata = new HashMap<>();
            metadata.put("fileId", fileId);
            metadata.put("bucket", bucketName);
            metadata.put("contentType", stat.contentType());
            metadata.put("size", String.valueOf(stat.size()));
            metadata.put("lastModified", stat.lastModified().toString());

            // Добавляем пользовательские метаданные
            if (stat.userMetadata() != null) {
                metadata.putAll(stat.userMetadata());
            }

            return ResponseEntity.ok(metadata);
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "File not found"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve file metadata"));
        }
    }

    @GetMapping("/download/{fileId:.+}")
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable String fileId,
            @RequestParam(required = false, defaultValue = "mybucket") String bucket,
            HttpServletResponse response) {

        try {
            // Получаем метаданные файла
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileId)
                            .build()
            );

            // Получаем поток файла из MinIO
            InputStream fileStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileId)
                            .build()
            );

            // Определяем оригинальное имя файла из метаданных
            String originalFilename = stat.userMetadata().getOrDefault("original-filename", fileId);

            // Устанавливаем заголовки ответа
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(stat.contentType()));
            headers.setContentLength(stat.size());
            headers.setContentDispositionFormData("attachment", originalFilename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            // Возвращаем файл как поток
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(fileStream));

        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error downloading file");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }


//    @GetMapping("/download/{fileId}")
//    public ResponseEntity<Resource> downloadFile(
//            @PathVariable String fileId
////            HttpServletResponse response
//    ) {
//        try {
//            // 1. Получаем метаданные файла
////            StatObjectResponse stat = minioClient.statObject(
////                    StatObjectArgs.builder()
////                            .bucket("mybucket")
////                            .object("user-uploads/" + fileId)
////                            .build()
////            );
//
//            // 2. Получаем сам файл
//            InputStream fileStream = minioClient.getObject(
//                    GetObjectArgs.builder()
//                            .bucket("mybucket")
//                             .object("user-uploads/" + fileId)
//          //                  .object(fileId)
//                            .build()
//            );
//
//            // 3. Подготавливаем ответ
//            InputStreamResource resource = new InputStreamResource(fileStream);
//
//            return ResponseEntity.ok()
//               //     .contentType(MediaType.parseMediaType(stat.contentType()))
//                    .header(HttpHeaders.CONTENT_DISPOSITION)
//                       //     "attachment; filename=\"" + stat.userMetadata().get("original-filename") + "\"")
//               //     .contentLength(stat.size())
//                    .body((Resource) resource);
//
//        } catch (ErrorResponseException e) {
//            if (e.errorResponse().code().equals("NoSuchKey")) {
//                return ResponseEntity.notFound().build();
//            }
//            return ResponseEntity.internalServerError().build();
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }

}
