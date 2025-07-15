package com.yazikochesalna.authorizationservice.config.properties

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@Component
@ConfigurationProperties(prefix = "front.server")
@Data
class FrontProperties {
    lateinit var origins: List<String>
}
