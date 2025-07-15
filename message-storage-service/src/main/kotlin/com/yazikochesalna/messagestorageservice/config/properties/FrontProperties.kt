package com.yazikochesalna.messagestorageservice.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@ConfigurationProperties(prefix = "frontend")
data class FrontProperties(
    val origins: List<String>
)
