package com.yazikochesalna.userservice.service.externalservice;

import com.yazikochesalna.common.service.JwtService;
import com.yazikochesalna.userservice.dto.LoginResponseDTO;
import com.yazikochesalna.userservice.dto.notificationdto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.naming.ServiceUnavailableException;
import java.time.Duration;

@Service
//@RequiredArgsConstructor
public class MessagingClientService {

    private final JwtService jwtService;
//    @Qualifier("messagingServiceWebClient")
    private final WebClient messagingWebClient;

    public MessagingClientService(
            JwtService jwtService, @Qualifier("messagingServiceWebClient") WebClient messagingWebClient) {
        this.jwtService = jwtService;
        this.messagingWebClient = messagingWebClient;
    }

    private final static String MESSAGING_URL = "/api/v1/ws/notification/new-user-avatar";

    @Value("${webclient.timeout}")
    private Long webClientTimeout;

    public void setNewAvatar(NotificationDTO notificationDTO) throws ServiceUnavailableException {
        messagingWebClient.post()
                .uri(MESSAGING_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generateServiceToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(notificationDTO)
                .retrieve()
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(webClientTimeout))
                .blockOptional(Duration.ofSeconds(webClientTimeout))
                .orElseThrow(() -> new ServiceUnavailableException("Failed to send avatar notification"));
    }

}
