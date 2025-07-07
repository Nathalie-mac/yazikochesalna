package com.yazikochesalna.chatservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "user.service")
@Data
public class UserServiceProperties {
    private String url;
}
