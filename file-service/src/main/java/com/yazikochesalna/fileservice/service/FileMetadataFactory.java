package com.yazikochesalna.fileservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import it.sauronsoftware.jave.MultimediaInfo;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.beans.Encoder;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileMetadataFactory {

    public Map<String, String> extractMetadataByContentType(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        String contentType = file.getContentType();

        if (contentType == null) {
            return metadata;
        }

        try (InputStream stream = file.getInputStream()) {
            // Для изображений
            if (contentType.startsWith("image/")) {
                BufferedImage img = ImageIO.read(stream);
                if (img != null) {
                    metadata.put("width", String.valueOf(img.getWidth()));
                    metadata.put("height", String.valueOf(img.getHeight()));
                }
            }

            // Для аудио/видео (нужно качать драйвер, пока решила не реализовывать)
//            else if (contentType.startsWith("audio/") || contentType.startsWith("video/")) {
//                metadata.putAll(extractMediaMetadata(file));
//            }
        } catch (Exception e) {
            log.error("Metadata extraction failed", e);
        }

        return metadata;
    }

    // для аудио/видео - не используется
    public Map<String, String> extractMediaMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();

        try {
            // Создаем временный файл
            Path tempFile = Files.createTempFile("media", ".tmp");
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            // Инициализируем FFprobe (укажите путь к бинарнику ffprobe)
            FFprobe ffprobe = new FFprobe("ffprobe"); // или полный путь "/usr/bin/ffprobe"
            FFmpegProbeResult probeResult = ffprobe.probe(tempFile.toString());

            // Основные метаданные
            metadata.put("duration", String.format("%.1f", probeResult.getFormat().duration));
            metadata.put("format", probeResult.getFormat().format_name);


            // Удаляем временный файл
            Files.delete(tempFile);

        } catch (Exception e) {
            log.error("Media metadata extraction failed", e);
        }

        return metadata;
    }
}
