package com.yazikochesalna.fileservice.controller;

import com.yazikochesalna.fileservice.advice.NotAttachedException;
import com.yazikochesalna.fileservice.advice.MinioFileNotFoundCustomException;
import com.yazikochesalna.fileservice.data.BaseFileInfo;
import com.yazikochesalna.fileservice.dto.RequestDTO;
import com.yazikochesalna.fileservice.dto.UploadResponseDTO;
import com.yazikochesalna.fileservice.service.*;
import io.minio.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Operation(summary = "Загрузить файл в хранилище",
            description = "Принимает файл, chatID, messageID или файл, userID. Возвращает fileUUID")
    public ResponseEntity<UploadResponseDTO> uploadFile(
            @RequestParam("file") @NotNull MultipartFile file,
            @ModelAttribute RequestDTO metadata) {

        try {
            if (file.isEmpty()) {
                throw new NotAttachedException("File is empty or not attached");
            }

            UploadResponseDTO response = uploadMinioService.uploadFileWithMetadata(file, metadata);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }


    @GetMapping("/metadata")
    @Operation(summary = "Получить метаданные файла (разные для картинок, аудио/видео и других файлов)",
            description = "Получает fileUUID и chatID или userID. Возвращает метаданные файла.")
    public ResponseEntity<BaseFileInfo> getFileMetadata(@ModelAttribute RequestDTO requestDTO)
            throws MinioFileNotFoundCustomException {

        if (requestDTO.getFileUUID() == null){
            throw new NotAttachedException("fileUUID not provided in metadata");
        }

        String folder = commonService.resolveFolderName(requestDTO);
        StatObjectResponse stat = commonService.getFileStat(folder + requestDTO.getFileUUID());

        BaseFileInfo fileInfo =  getFileMetadataService.buildFileInfo(requestDTO.getFileUUID(), stat);

        return ResponseEntity.ok(fileInfo);
    }


    @GetMapping("/download")
    @Operation(summary = "Скачать файл",
            description = "Получает fileUUID и chatID или userID. Возвращает файл")
    public ResponseEntity<InputStreamResource> downloadFile(
            @Valid @ModelAttribute RequestDTO requestDTO) throws MinioFileNotFoundCustomException {

        if (requestDTO.getFileUUID() == null){
            throw new NotAttachedException("fileUUID not provided in metadata");
        }

        String objectPath = commonService.resolveFolderName(requestDTO) + requestDTO.getFileUUID();

        InputStream fileStream = downloadMinioService.getFileStream(objectPath);
        StatObjectResponse stat = commonService.getFileStat(objectPath);

        HttpHeaders headers = downloadMinioService.createResponseHeaders(stat, objectPath);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(fileStream));
    }

}
