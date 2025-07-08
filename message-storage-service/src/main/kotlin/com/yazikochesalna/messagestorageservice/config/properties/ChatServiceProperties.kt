package com.yazikochesalna.messagestorageservice.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding


@ConfigurationProperties(prefix = "chat.service")
data class ChatServiceProperties(
    val url: String
)
