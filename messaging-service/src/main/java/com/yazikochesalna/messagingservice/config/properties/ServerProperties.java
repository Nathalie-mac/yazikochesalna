package com.yazikochesalna.messagingservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "server")
public class ServerProperties {
    private Integer port;

}
