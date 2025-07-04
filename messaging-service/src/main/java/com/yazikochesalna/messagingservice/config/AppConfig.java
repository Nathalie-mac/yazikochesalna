package com.yazikochesalna.messagingservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.time.Duration;

@Configuration
public class AppConfig {

    @Value("${chat.service.url}")
    private String chatServiceBaseUrl;
    @Bean
    public WebClient.Builder webClientBuilder() {
        // Настройка HttpClient для WebClient с таймаутами
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofSeconds(5))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS)));

        return WebClient.builder()
                .baseUrl(chatServiceBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }

    @Bean
    public WebClient chatServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Опционально: задать конкретный формат для Instant
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

        return mapper;
    }

}
