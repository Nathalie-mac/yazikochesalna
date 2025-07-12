package com.yazikochesalna.fileservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class ContentTypeMetadataExtractor {

    private static final String DIGITS_ONLY_REGEX = "\\d+";

    public Map<String, String> extractMetadataByContentType(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        String contentType = file.getContentType();

        if (contentType == null) {
            return metadata;
        }

        try (InputStream stream = file.getInputStream()) {
            if (contentType.startsWith("image/")) {
                BufferedImage img = ImageIO.read(stream);
                if (img != null) {
                    metadata.put("width", String.valueOf(img.getWidth()));
                    metadata.put("height", String.valueOf(img.getHeight()));
                }
            }

            else if (contentType.startsWith("audio/") || contentType.startsWith("video/")) {
                metadata.putAll(extractMediaMetadata(file));
            }

        } catch (Exception e) {
            log.error("Metadata extraction failed", e);
        }

        return metadata;
    }

    public Map<String, String> extractMediaMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();

        try (InputStream stream = file.getInputStream()) {
            Metadata tikaMetadata = new Metadata();
            ParseContext context = new ParseContext();

            Parser parser = selectParser(file.getContentType());
            parser.parse(stream, new BodyContentHandler(), tikaMetadata, context);

            String duration = getDuration(tikaMetadata);
            addIfNotNull(metadata, "duration", duration);

        } catch (Exception e) {
            log.error("Media metadata extraction failed", e);
        }

        return metadata;
    }

    private Parser selectParser(String contentType) {
        if (contentType != null) {
            if (contentType.equals("audio/mpeg")) {
                return new Mp3Parser();
            } else if (contentType.equals("video/mp4")) {
                return new MP4Parser();
            }
        }
        return new AutoDetectParser();
    }

    private String getDuration(Metadata metadata) {
        var durationKeys = List.of(
                "duration",
                "xmpDM:duration",
                "TimeLength",
                "length"
        );

        for (String key : durationKeys) {
            String duration = metadata.get(key);
            if (duration != null && !duration.isEmpty()) {
                return formatDuration(duration);
            }
        }
        return null;
    }

    private String formatDuration(String rawDuration) {
        try {
            // Если длительность аудио/видео в мс (только из цифр, например, "1500" — 1500 мс),
            // конвертируем в секунды с двумя знаками после запятой ("1.50").
            if (rawDuration.matches(DIGITS_ONLY_REGEX)) {
                long millis = Long.parseLong(rawDuration);
                return String.format("%.2f", millis / 1000.0);
            }
            // Иначе возвращаем как есть (например, "1.5s", "12:34").
            return rawDuration;
        } catch (NumberFormatException e) {
            return rawDuration;
        }
    }

    private void addIfNotNull(Map<String, String> map, String key, String value) {
        if (value != null && !value.trim().isEmpty()) {
            map.put(key, value);
        }
    }

}
