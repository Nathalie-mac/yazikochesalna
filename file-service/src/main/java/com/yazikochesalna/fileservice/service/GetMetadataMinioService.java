package com.yazikochesalna.fileservice.service;

import com.yazikochesalna.fileservice.advice.MinioFileNotFoundCustomException;
import com.yazikochesalna.fileservice.advice.MinioRuntimeCustomException;
import com.yazikochesalna.fileservice.data.BaseFileInfo;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GetMetadataMinioService {

    @Autowired
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String BUCKET;

//    public StatObjectResponse getStatObject(String objectPath)
//            throws MinioFileNotFoundCustomException, MinioRuntimeCustomException {
//        try {
//            return minioClient.statObject(
//                    StatObjectArgs.builder()
//                            .bucket(BUCKET)
//                            .object(objectPath)
//                            .build());
//        } catch (ErrorResponseException e) {
//            if (e.errorResponse().code().equals("NoSuchKey")) {
//                throw new MinioFileNotFoundCustomException("File not found: " + objectPath);
//            }
//            throw new MinioRuntimeCustomException("Minio error: " + e.getMessage());
//        } catch (Exception e) {
//            throw new MinioRuntimeCustomException("Error accessing file metadata: " + e.getMessage());
//        }
//    }

    public BaseFileInfo buildFileInfo(String fileUuid, StatObjectResponse stat) {
        BaseFileInfo fileInfo = new BaseFileInfo();
        fileInfo.setStorageFileName(fileUuid);
        fileInfo.setContentType(stat.contentType());
        fileInfo.setSize(stat.size());
        fileInfo.setLastModified(stat.lastModified() != null ?
                stat.lastModified().toLocalDateTime() : null);

        if (stat.userMetadata() != null) {
            Map<String, String> userMetadata = stat.userMetadata();
            processUserMetadata(fileInfo, userMetadata);
        }

        return fileInfo;
    }

    private void processUserMetadata(BaseFileInfo fileInfo, Map<String, String> userMetadata) {
        fileInfo.setOriginalFileName(userMetadata.get("original-filename"));

        Optional.ofNullable(userMetadata.get("message-uuid"))
                .ifPresent(fileInfo::setMessageUuid);

        Optional.ofNullable(userMetadata.get("chat-id"))
                .map(Long::parseLong)
                .ifPresent(fileInfo::setChatId);

        Optional.ofNullable(userMetadata.get("user-id"))
                .map(Long::parseLong)
                .ifPresent(fileInfo::setUserId);

        Map<String, Object> specificData = new HashMap<>(userMetadata);
        Stream.of("original-filename", "message-uuid", "chat-id", "user-id")
                .forEach(specificData::remove);

        if (!specificData.isEmpty()) {
            fileInfo.setSpecificData(specificData);
        }
    }
}
