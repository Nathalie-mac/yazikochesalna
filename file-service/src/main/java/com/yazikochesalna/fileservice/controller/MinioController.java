package com.yazikochesalna.fileservice.controller;

import com.yazikochesalna.fileservice.advice.FileNotAttachedException;
import com.yazikochesalna.fileservice.advice.MinioFileNotFoundCustomException;
import com.yazikochesalna.fileservice.advice.MinioRuntimeCustomException;
import com.yazikochesalna.fileservice.advice.MinioUploadCustomException;
import com.yazikochesalna.fileservice.data.BaseFileInfo;
import com.yazikochesalna.fileservice.dto.RequestDTO;
import com.yazikochesalna.fileservice.dto.UploadResponseDTO;
import com.yazikochesalna.fileservice.service.*;
import io.minio.*;
import io.minio.errors.MinioException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/files")
@Tag(name = "Minio API")
public class MinioController {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String BUCKET;

    private final UploadMinioService uploadMinioService;
    private final GetMetadataMinioService getFileMetadataService;
    private final DownloadMinioService downloadMinioService;
    private final CommonService commonService;


    @PostMapping("/upload")
//    @RolesAllowed("SERVICE")
//    @Hidden
    public ResponseEntity<UploadResponseDTO> uploadFile(
            @RequestParam("file") @NotNull MultipartFile file,
            @ModelAttribute RequestDTO metadata) {

        try {
            if (file.isEmpty()) {
                throw new FileNotAttachedException("File is empty or not attached");
            }

            UploadResponseDTO response = uploadMinioService.uploadFileWithMetadata(file, metadata);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (MinioException e) {
            throw new MinioUploadCustomException("Failed to upload file to MinIO: " + e.getMessage());
        } catch (Exception e) {
            throw new MinioRuntimeCustomException("Internal server error during file upload: " + e.getMessage());
        }
    }
//    @PostMapping("/upload")
//    @RolesAllowed("SERVICE")
//    @Hidden
//    public ResponseEntity<UploadResponseDTO> uploadFile(
//            @RequestParam("file") @NotNull MultipartFile file,
//            @ModelAttribute RequestDTO metadata
//    ) throws Exception {
//
//        String folderName = commonService.resolveFolderName(metadata);
//        String fileId = String.valueOf(UUID.randomUUID());
//        String objectName = folderName + fileId;
//        Map<String, String> userMetadata = uploadMinioService.buildMetadata(file, metadata);
//
//        uploadMinioService.uploadFile(file, objectName, userMetadata);
//
//        return ResponseEntity.ok(new UploadResponseDTO(fileId));
//    }

    @GetMapping("/metadata")
//    @RolesAllowed("SERVICE")
//    @Hidden
    public ResponseEntity<BaseFileInfo> getFileMetadata(@ModelAttribute RequestDTO requestDTO)
            throws MinioFileNotFoundCustomException {
        String folder = commonService.resolveFolderName(requestDTO);
        StatObjectResponse stat = commonService.getFileStat(folder + requestDTO.getFileUUID());

        BaseFileInfo fileInfo =  getFileMetadataService.buildFileInfo(requestDTO.getFileUUID(), stat);

        return ResponseEntity.ok(fileInfo);
    }


    @GetMapping("/download")
//    @RolesAllowed("SERVICE")
//    @Hidden
    public ResponseEntity<InputStreamResource> downloadFile(
            @Valid @ModelAttribute RequestDTO requestDTO) throws MinioFileNotFoundCustomException {

        String objectPath = commonService.resolveFolderName(requestDTO) + requestDTO.getFileUUID();

        InputStream fileStream = downloadMinioService.getFileStream(objectPath);
        StatObjectResponse stat = commonService.getFileStat(objectPath);

        HttpHeaders headers = downloadMinioService.createResponseHeaders(stat, objectPath);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(fileStream));
    }

}
