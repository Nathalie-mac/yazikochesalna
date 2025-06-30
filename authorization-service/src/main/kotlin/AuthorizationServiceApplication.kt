package com.yazikochesalna.authorizationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [
    "com.yazikochesalna.authorizationservice",
    "com.yazikochesalna.common",
    "org.springframework.security.crypto.password",
]
)
open class AuthorizationServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationServiceApplication>(*args)
}