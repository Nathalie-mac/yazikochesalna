package com.yazikochesalna.messagestorageservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class], scanBasePackages =
[
    "com.yazikochesalna.messagestorageservice",
    "com.yazikochesalna.common",
])
open class MessageStorageServiceApplication

fun main(args: Array<String>) {
    runApplication<MessageStorageServiceApplication>(*args)
}