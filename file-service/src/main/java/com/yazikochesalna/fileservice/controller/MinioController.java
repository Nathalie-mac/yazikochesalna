package com.yazikochesalna.fileservice.controller;

import com.yazikochesalna.fileservice.advice.MinioFileNotFoundCustomException;
import com.yazikochesalna.fileservice.data.BaseFileInfo;
import com.yazikochesalna.fileservice.dto.RequestDTO;
import com.yazikochesalna.fileservice.dto.UploadResponseDTO;
import com.yazikochesalna.fileservice.service.*;
import io.minio.*;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @RolesAllowed("SERVICE")
    @Hidden
    public ResponseEntity<UploadResponseDTO> uploadFile(
            @RequestParam("file") @NotNull MultipartFile file,
            @ModelAttribute RequestDTO metadata
    ) throws Exception {

        String folderName = commonService.resolveFolderName(metadata);
        String fileId = String.valueOf(UUID.randomUUID());
        String objectName = folderName + fileId;
        Map<String, String> userMetadata = uploadMinioService.buildMetadata(file, metadata);

        uploadMinioService.uploadFile(file, objectName, userMetadata);

        return ResponseEntity.ok(new UploadResponseDTO(fileId));
    }

    @GetMapping("/metadata")
    @RolesAllowed("SERVICE")
    @Hidden
    public ResponseEntity<BaseFileInfo> getFileMetadata(@ModelAttribute RequestDTO requestDTO)
            throws MinioFileNotFoundCustomException {
        String folder = commonService.resolveFolderName(requestDTO);
        StatObjectResponse stat = commonService.getFileStat(folder + requestDTO.getFileUuid());

        BaseFileInfo fileInfo =  getFileMetadataService.buildFileInfo(requestDTO.getFileUuid(), stat);

        return ResponseEntity.ok(fileInfo);
    }


    @GetMapping("/download")
    @RolesAllowed("SERVICE")
    @Hidden
    public ResponseEntity<InputStreamResource> downloadFile(
            @Valid @ModelAttribute RequestDTO requestDTO) throws MinioFileNotFoundCustomException {

        String objectPath = commonService.resolveFolderName(requestDTO) + requestDTO.getFileUuid();

        InputStream fileStream = downloadMinioService.getFileStream(objectPath);
        StatObjectResponse stat = commonService.getFileStat(objectPath);

        HttpHeaders headers = downloadMinioService.createResponseHeaders(stat, objectPath);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(fileStream));
    }

}
