package com.yazikochesalna.fileservice.service;

import com.yazikochesalna.fileservice.data.BaseFileInfo;
import com.yazikochesalna.fileservice.data.MetadataKeys;
import io.minio.MinioClient;
import io.minio.StatObjectResponse;
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
        fileInfo.setOriginalFileName(userMetadata.get(MetadataKeys.ORIGINAL_FILENAME.getKey()));

        Optional.ofNullable(userMetadata.get(MetadataKeys.MESSAGE_UUID.getKey()))
                .ifPresent(fileInfo::setMessageUUID);

        Optional.ofNullable(userMetadata.get(MetadataKeys.CHAT_ID.getKey()))
                .map(Long::parseLong)
                .ifPresent(fileInfo::setChatID);

        Optional.ofNullable(userMetadata.get(MetadataKeys.USER_ID.getKey()))
                .map(Long::parseLong)
                .ifPresent(fileInfo::setUserID);

        Map<String, Object> specificData = new HashMap<>(userMetadata);
        Stream.of(MetadataKeys.values())
                .map(MetadataKeys::getKey)
                .forEach(specificData::remove);

        if (!specificData.isEmpty()) {
            fileInfo.setSpecificData(specificData);
        }
    }
}
