package com.yazikochesalna.messagingservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Chat-service", version = "v1"))
@SecuritySchemes({
        @SecurityScheme(
                name = "bearerAuth",
                description = "JWT Auth",
                scheme = "bearer",
                bearerFormat = "JWT",
                type = SecuritySchemeType.HTTP,
                in = SecuritySchemeIn.HEADER,
                paramName = "Authorization"
        )
})
public class OpenApiConfig {
}