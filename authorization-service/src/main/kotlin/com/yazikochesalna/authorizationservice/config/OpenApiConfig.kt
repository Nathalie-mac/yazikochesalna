package com.yazikochesalna.authorizationservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.StringUtils
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.Components

@Configuration
open class OpenApiConfig(
    @Value("\${spring.application.name}")
    private val moduleName: String
) {
    private val apiVersion: String = "v2"

    @Bean
    open fun customOpenAPI(): OpenAPI {
        val securitySchemeName = "bearerAuth"
        val formattedModuleName = if (StringUtils.hasText(moduleName)) moduleName else "NoName - contact admin"
        val apiTitle = "${formattedModuleName.replaceFirstChar { it.uppercase() }} API"

        return OpenAPI()
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(
                Components().addSecuritySchemes(
                    securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            )
            .info(Info().title(apiTitle).version(apiVersion))
    }
}