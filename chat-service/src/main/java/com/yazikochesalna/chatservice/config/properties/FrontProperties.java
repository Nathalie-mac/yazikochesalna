package com.yazikochesalna.chatservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "front.server")
@Data
public class FrontProperties {

    private List<String> origins;
}
