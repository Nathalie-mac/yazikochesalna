package com.yazikochesalna.fileservice.service;

import com.yazikochesalna.common.service.JwtService;
import com.yazikochesalna.fileservice.dto.ProfilePictureUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.naming.ServiceUnavailableException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserClientService {

    private final JwtService jwtService;
    private final WebClient userServiceWebClient;


    @Value("${webclient.timeout}")
    private Long webClientTimeout;

    private static final String USER_SERVICE_URL = "/api/v1/users/update-file";

    public void updateFileUuid(Long userId, String fileUUID) throws ServiceUnavailableException {
        ProfilePictureUpdateRequestDTO request = new ProfilePictureUpdateRequestDTO(userId, fileUUID);

        userServiceWebClient.patch()
                .uri(USER_SERVICE_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generateServiceToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(webClientTimeout))
                .blockOptional(Duration.ofSeconds(webClientTimeout))
                .orElseThrow(() -> new ServiceUnavailableException("Failed to update file UUID"));
    }
}
