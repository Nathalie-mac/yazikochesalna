package com.yazikochesalna.fileservice.service;

import com.yazikochesalna.fileservice.advice.MinioFileNotFoundCustomException;
import com.yazikochesalna.fileservice.advice.MinioRuntimeCustomException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DownloadMinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String BUCKET;

    public InputStream getFileStream(String objectPath)
            throws MinioRuntimeCustomException {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(BUCKET)
                            .object(objectPath)
                            .build());
        } catch (Exception e) {
            throw new MinioRuntimeCustomException("Failed to get file stream: " + e.getMessage());
        }
    }

    //в получениии метаданных такой же
//    public StatObjectResponse getFileStat(String objectPath)
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
//            throw new MinioRuntimeCustomException("Error getting file stats: " + e.getMessage());
//        } catch (Exception e) {
//            throw new MinioRuntimeCustomException("Internal error getting file stats: " + e.getMessage());
//        }
//    }

    public HttpHeaders createResponseHeaders(StatObjectResponse stat, String defaultFilename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(stat.contentType()));
        headers.setContentLength(stat.size());
        headers.setContentDispositionFormData(
                "attachment",
                Optional.ofNullable(stat.userMetadata())
                        .map(meta -> meta.get("original-filename"))
                        .orElse(defaultFilename)
        );
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        return headers;
    }

}
