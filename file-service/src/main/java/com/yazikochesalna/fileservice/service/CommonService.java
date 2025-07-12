package com.yazikochesalna.fileservice.service;

import com.yazikochesalna.fileservice.advice.MinioFileNotFoundCustomException;
import com.yazikochesalna.fileservice.advice.MinioServerCustomException;
import com.yazikochesalna.fileservice.advice.NotAttachedException;
import com.yazikochesalna.fileservice.dto.RequestDTO;
import io.minio.*;
import io.minio.errors.*;
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
    public void isCreatedBucket() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(BUCKET)
                    .build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(BUCKET)
                        .build());
            }
        } catch (Exception e) {
            throw new MinioServerCustomException("Failed to initialize MinIO bucket: " + e.getMessage());
        }
    }

    public String resolveFolderName(RequestDTO metadata) {
        if (metadata == null) {
            throw new NotAttachedException("Metadata cannot be null");
        }
        if (metadata.getChatID() != null) {
            return "chatId" + String.valueOf(metadata.getChatID()) + "/";
        } else if (metadata.getUserID() != null) {
            return "userId" + String.valueOf(metadata.getUserID()) + "/";
        }
        throw new NotAttachedException("Neither chatID nor userID provided in metadata");
    }

    public StatObjectResponse getFileStat(String objectPath)
    {
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
            throw new MinioServerCustomException("Error getting file stats: " + e.getMessage());
        }
        catch (Exception e) {
            throw new MinioServerCustomException("Internal error getting file stats: " + e.getMessage());
        }
    }
}
