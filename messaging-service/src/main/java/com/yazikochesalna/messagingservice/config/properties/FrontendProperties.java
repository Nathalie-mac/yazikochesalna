package com.yazikochesalna.messagingservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "frontend")
public class FrontendProperties {
    private List<String> origins;


}
