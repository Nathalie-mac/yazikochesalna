package com.yazikochesalna.fileservice.service;

import com.yazikochesalna.fileservice.advice.MinioFileNotFoundCustomException;
import com.yazikochesalna.fileservice.advice.MinioRuntimeCustomException;
import com.yazikochesalna.fileservice.dto.RequestDTO;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String BUCKET;

    @SneakyThrows
    public void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(BUCKET)
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(BUCKET)
                    .build());
        }
    }

    public String resolveFolderName(RequestDTO metadata) {
        if (metadata.getChatId() != null) {
            return "chatId" + String.valueOf(metadata.getChatId()) + "/";
        } else if (metadata.getUserId() != null) {
            return "userId" + String.valueOf(metadata.getUserId()) + "/";
        }
        return "";
    }

    public StatObjectResponse getFileStat(String objectPath)
            throws MinioFileNotFoundCustomException, MinioRuntimeCustomException {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(BUCKET)
                            .object(objectPath)
                            .build());
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                throw new MinioFileNotFoundCustomException("File not found: " + objectPath);
            }
            throw new MinioRuntimeCustomException("Error getting file stats: " + e.getMessage());
        } catch (Exception e) {
            throw new MinioRuntimeCustomException("Internal error getting file stats: " + e.getMessage());
        }
    }
}
