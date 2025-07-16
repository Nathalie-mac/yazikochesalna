package com.yazikochesalna.fileservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "front.server")
@Data
public class FrontProperties {
    private String url;
}
