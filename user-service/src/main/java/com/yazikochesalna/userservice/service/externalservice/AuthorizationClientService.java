package com.yazikochesalna.userservice.service.externalservice;

import com.yazikochesalna.common.service.JwtService;
import com.yazikochesalna.userservice.dto.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.naming.ServiceUnavailableException;
import java.time.Duration;

@Service
public class AuthorizationClientService {

    private final JwtService jwtService;
    private final WebClient authServiceWebClient;

    public AuthorizationClientService(
            JwtService jwtService, @Qualifier("authServiceWebClient") WebClient authServiceWebClient) {
        this.jwtService = jwtService;
        this.authServiceWebClient = authServiceWebClient;
    }

    private final static String AUTH_URL = "/api/v1/auth/getlogin?userID={userId}";

    @Value("${webclient.timeout}")
    private Long webClientTimeout;

    public String getUserLogin(long userId) throws ServiceUnavailableException {
        return authServiceWebClient.get()
                .uri(AUTH_URL, userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generateServiceToken())
                .retrieve()
                .bodyToMono(LoginResponseDTO.class)
                .timeout(Duration.ofSeconds(webClientTimeout))
                .blockOptional(Duration.ofSeconds(webClientTimeout))
                .orElseThrow(() -> new ServiceUnavailableException("Failed to get login"))
                .getLogin();
    }
}
