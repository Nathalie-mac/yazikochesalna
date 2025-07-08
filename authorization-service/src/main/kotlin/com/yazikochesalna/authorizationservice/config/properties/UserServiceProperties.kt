package com.yazikochesalna.authorizationservice.com.yazikochesalna.authorizationservice.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@Component
@ConfigurationProperties(prefix = "user.service")
class UserServiceProperties {
    lateinit var url: String
}
