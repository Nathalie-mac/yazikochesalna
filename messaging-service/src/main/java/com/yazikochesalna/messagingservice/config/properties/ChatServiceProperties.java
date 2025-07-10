package com.yazikochesalna.messagingservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "chat.service")
public class ChatServiceProperties {
    private String url;


}
