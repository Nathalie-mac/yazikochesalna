package com.yazikochesaina.authorizationservi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
open class MessageStorageServiceApplication

fun main(args: Array<String>) {
    runApplication<MessageStorageServiceApplication>(*args)
}