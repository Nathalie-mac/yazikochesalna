package com.yazikochesalna.fileservice.service;

import com.yazikochesalna.fileservice.advice.MinioRuntimeCustomException;
import com.yazikochesalna.fileservice.advice.MinioUploadCustomException;
import com.yazikochesalna.fileservice.data.MetadataKeys;
import com.yazikochesalna.fileservice.dto.RequestDTO;
import com.yazikochesalna.fileservice.dto.UploadResponseDTO;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    private static final long MINIO_AUTO_PART_SIZE = -1;

    private final ContentTypeMetadataExtractor contentTypeMetadataExtractor;
    private final CommonService commonService;

    public UploadResponseDTO uploadFileWithMetadata(MultipartFile file, RequestDTO metadata)
            throws IOException, MinioException {

        String objectName = generateFolderName(metadata);
        Map<String, String> userMetadata = buildMetadata(file, metadata);

        uploadFile(file, objectName, userMetadata);

        return new UploadResponseDTO(extractFileIdFromObjectName(objectName));
    }

    public void uploadFile(MultipartFile file, String objectName, Map<String, String> userMetadata)
    {
        commonService.isCreatedBucket();

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), MINIO_AUTO_PART_SIZE)
                            .contentType(file.getContentType())
                            .userMetadata(userMetadata)
                            .build()
            );
        } catch (ErrorResponseException e) {
            throw new MinioUploadCustomException("Failed to upload file: " + e.getMessage());
        } catch (InsufficientDataException | InternalException e) {
            throw new MinioRuntimeCustomException("MinIO internal error: " + e.getMessage());
        } catch (InvalidResponseException | XmlParserException | ServerException |
                 InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            throw new MinioRuntimeCustomException("Error during file upload: " + e.getMessage());
        }
    }

    public Map<String, String> buildMetadata(MultipartFile file, RequestDTO metadata) throws IOException {
        Map<String, String> userMetadata = new HashMap<>();

        addBasicMetadata(userMetadata, file);
        addServiceMetadata(userMetadata, metadata);

        return userMetadata;
    }

    private void addBasicMetadata(Map<String, String> metadata, MultipartFile file) {
        metadata.put(MetadataKeys.ORIGINAL_FILENAME.getKey(), file.getOriginalFilename());
        metadata.putAll(contentTypeMetadataExtractor.extractMetadataByContentType(file));
    }

    private void addServiceMetadata(Map<String, String> metadata, RequestDTO dto) {
        if (dto.getChatID() != null && dto.getMessageUUID() != null) {
            metadata.put(MetadataKeys.CHAT_ID.getKey(), String.valueOf(dto.getChatID()));
            metadata.put(MetadataKeys.MESSAGE_UUID.getKey(), dto.getMessageUUID());
        } else if (dto.getUserID() != null) {
            metadata.put(MetadataKeys.USER_ID.getKey(), String.valueOf(dto.getUserID()));
        }
    }

    public String generateFolderName(RequestDTO metadata) {
        String folderName = commonService.resolveFolderName(metadata);
        return folderName + UUID.randomUUID();
    }

    private String extractFileIdFromObjectName(String objectName) {
        return objectName.substring(objectName.lastIndexOf('/') + 1);
    }
}
