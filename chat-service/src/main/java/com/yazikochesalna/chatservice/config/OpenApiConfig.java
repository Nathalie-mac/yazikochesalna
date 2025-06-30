package com.yazikochesalna.chatservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;

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