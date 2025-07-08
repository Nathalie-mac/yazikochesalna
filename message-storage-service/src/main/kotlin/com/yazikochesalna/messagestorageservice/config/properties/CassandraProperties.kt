package com.yazikochesalna.messagestorageservice.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.cassandra")
data class CassandraProperties(
    val username: String,
    val password: String,
    val keyspaceName: String,
    val contactPoints: String,
    val localDatacenter: String,
    val port: Int
)
