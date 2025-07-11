package com.yazikochesalna.fileservice.service;

import com.yazikochesalna.fileservice.dto.RequestDTO;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadMinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String BUCKET;

    private static final String ORIGINAL_FILENAME_KEY = "original-filename";
    private static final String CHAT_ID_KEY = "chat-id";
    private static final String MESSAGE_UUID_KEY = "message-uuid";
    private static final String USER_ID_KEY = "user-id";

    private final ContentTypeMetadataExtractor contentTypeMetadataExtractor;
    private final CommonService commonService;

    public void uploadFile(MultipartFile file, String objectName, Map<String, String> userMetadata)
            throws IOException, MinioException, ErrorResponseException, Exception {

        commonService.createBucket();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .userMetadata(userMetadata)
                        .build()
        );
    }

    public Map<String, String> buildMetadata(MultipartFile file, RequestDTO metadata) throws IOException {
        Map<String, String> userMetadata = new HashMap<>();

        addBasicMetadata(userMetadata, file);
        addServiceMetadata(userMetadata, metadata);

        return userMetadata;
    }

    private void addBasicMetadata(Map<String, String> metadata, MultipartFile file) {
        metadata.put(ORIGINAL_FILENAME_KEY, file.getOriginalFilename());
        metadata.putAll(contentTypeMetadataExtractor.extractMetadataByContentType(file));
    }

    private void addServiceMetadata(Map<String, String> metadata, RequestDTO dto) {
        if (dto.getChatId() != null && dto.getMessageUuid() != null) {
            metadata.put(CHAT_ID_KEY, String.valueOf(dto.getChatId()));
            metadata.put(MESSAGE_UUID_KEY, dto.getMessageUuid());
        } else if (dto.getUserId() != null) {
            metadata.put(USER_ID_KEY, String.valueOf(dto.getUserId()));
        }
    }


    ///
//    @SneakyThrows
//    public void createBucket() {
//        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
//                .bucket(BUCKET)
//                .build());
//        if (!found) {
//            minioClient.makeBucket(MakeBucketArgs.builder()
//                    .bucket(BUCKET)
//                    .build());
//        }
//    }

    public String generateFolderName(RequestDTO metadata) {
        String folderName = commonService.resolveFolderName(metadata);
        return folderName + UUID.randomUUID();
    }

    /// /
//    public String resolveFolderName(RequestDTO metadata) {
//        if (metadata.getChatId() != null) {
//            return "chatId" + String.valueOf(metadata.getChatId()) + "/";
//        } else if (metadata.getUserId() != null) {
//            return "userId" + String.valueOf(metadata.getUserId()) + "/";
//        }
//        return "";
//    }

}
