package com.yazikochesalna.messagestorageservice.config

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs
import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry
import com.yazikochesalna.messagestorageservice.config.properties.CassandraProperties
import com.yazikochesalna.messagestorageservice.model.codecs.DateTimeCodec
import com.yazikochesalna.messagestorageservice.repository.AttachmentRepository
import com.yazikochesalna.messagestorageservice.repository.MessageRepository
//import com.yazikochesalna.messagestorageservice.model.codecs.MessageCodec
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate
import org.springframework.data.cassandra.core.cql.ReactiveCqlTemplate
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories
import java.net.InetSocketAddress

@EnableConfigurationProperties(CassandraProperties::class)
@Configuration
@EnableCassandraRepositories(basePackageClasses = [AttachmentRepository::class, MessageRepository::class])
open class AppConfig {

    @Bean
    open fun session(cassandraProperties: CassandraProperties, dateTimeCodec: DateTimeCodec): CqlSession {
        return CqlSession.builder()
            .withKeyspace(cassandraProperties.keyspaceName)
            .addContactPoint(InetSocketAddress(cassandraProperties.contactPoints, cassandraProperties.port))
            .withAuthCredentials(cassandraProperties.username, cassandraProperties.password)
            .withLocalDatacenter(cassandraProperties.localDatacenter)
            .addTypeCodecs(dateTimeCodec)
            .build()
    }
}