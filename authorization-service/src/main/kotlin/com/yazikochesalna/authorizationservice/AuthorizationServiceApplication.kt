package com.yazikochesalna.authorizationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
    "com.yazikochesalna.authorizationservice",
    "com.yazikochesalna.common",
    "org.springframework.security.crypto.password",
    "org.springframework.security.authentication",
],
    exclude = [ErrorMvcAutoConfiguration::class],
)
open class AuthorizationServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationServiceApplication>(*args)
}